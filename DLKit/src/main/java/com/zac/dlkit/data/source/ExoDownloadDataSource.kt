package com.zac.dlkit.data.source

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.drm.DrmSessionEventListener
import com.google.android.exoplayer2.drm.OfflineLicenseHelper
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.util.Util
import com.zac.dlkit.data.di.DownloadDataSourceInject.DOWNLOAD_NOTIFICATION_CHANNEL_ID
import com.zac.dlkit.data.service.DLDownloadService
import com.zac.dlkit.presentation.entity.DownloadConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference

class ExoDownloadDataSource(
    private val context: Context,
    private val downloadManager: DownloadManager,
    private val downloadTracker: DownloadTracker,
    private val httpDataSourceFactory: HttpDataSource.Factory,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DownloadDataSource {

    private val helperMap = mutableMapOf<String, DownloadHelper>()

    init {
        downloadManagerAtom.set(
            downloadManager.apply {
                // add listener
            }
        )

        downloadNotificationHelperAtom.set(
            DownloadNotificationHelper(context, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
        )
    }

    override fun createDownloadHelper(downloadConfig: DownloadConfig) {
        val builder = MediaItem.Builder()
            .setUri(downloadConfig.mpdUrl)
            .setMimeType(MIME_TYPE)

        downloadConfig.drmServerUrl?.let {
            builder.setDrmUuid(Util.getDrmUuid("widevine"))
                .setDrmLicenseUri(Uri.parse(it))
        }
        downloadConfig.drmRequestHeader?.let {
            builder.setDrmLicenseRequestHeaders(it)
        }

        val mediaItem = builder.build()
        val helper =  DownloadHelper.forMediaItem(
            context,
            mediaItem,
            DefaultRenderersFactory(context).apply {
                setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
            },
            httpDataSourceFactory
        )

        helperMap[downloadConfig.mpdUrl] = helper
    }

    override suspend fun prepareDownloadHelper(downloadConfig: DownloadConfig) = callbackFlow {
        val callback = object : DownloadHelper.Callback {
            override fun onPrepared(helper: DownloadHelper) {
                trySend(Result.success(Unit))
            }

            override fun onPrepareError(helper: DownloadHelper, e: IOException) {
                trySend(Result.failure(e))
            }
        }
        helperMap[downloadConfig.mpdUrl]?.prepare(callback)

        awaitClose {
            // do nothing
        }
    }.flowOn(dispatcher)

    override fun downloadDrmLicense(downloadConfig: DownloadConfig): List<Byte>? {
        val offlineLicenseHelper = getOfflineLicenseHelper(downloadConfig)
        val format = getFormat(downloadConfig)

        return try {
            format?.let {
                offlineLicenseHelper.downloadLicense(it).toList()
            }
        } catch (e: Exception) {
            offlineLicenseHelper.release()
            null
        }
    }

    override fun downloadContent(downloadConfig: DownloadConfig, keySet: List<Byte>?) {
        var downloadRequest = helperMap[downloadConfig.mpdUrl]?.getDownloadRequest(null) ?: return

        keySet?.let {
            downloadRequest = downloadRequest.copyWithKeySetId(it.toByteArray())
        }

        DownloadService.sendAddDownload(
            context,
            DLDownloadService::class.java,
            downloadRequest,
            false
        )
    }

    private fun getOfflineLicenseHelper(downloadConfig: DownloadConfig): OfflineLicenseHelper {
        val factory = httpDataSourceFactory
        downloadConfig.drmRequestHeader?.let {
            factory.setDefaultRequestProperties(it)
        }

        return OfflineLicenseHelper.newWidevineInstance(
            downloadConfig.mpdUrl,
            false,
            factory,
            downloadConfig.drmRequestHeader,
            DrmSessionEventListener.EventDispatcher()
        )
    }

    private fun getFormat(
        downloadConfig: DownloadConfig
    ): Format? {
        val helper = helperMap[downloadConfig.mpdUrl] ?: return null

        for (periodIndex in 0 until helper.periodCount) {
            val mappedTrackInfo = helper.getMappedTrackInfo(periodIndex)
            for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
                val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
                for (trackGroupIndex in 0 until trackGroups.length) {
                    val trackGroup = trackGroups[trackGroupIndex]
                    for (formatIndex in 0 until trackGroup.length) {
                        val format = trackGroup.getFormat(trackGroupIndex)
                        if (format.drmInitData != null) {
                            return format
                        }
                    }
                }
            }
        }
        return null
    }

    override fun releaseDownloadHelper(downloadConfig: DownloadConfig) {
        helperMap[downloadConfig.mpdUrl]?.release()
    }

    companion object {
        private const val MIME_TYPE = "application/dash+xml"

        val downloadManagerAtom: AtomicReference<DownloadManager> = AtomicReference(null)
        val downloadNotificationHelperAtom: AtomicReference<DownloadNotificationHelper> = AtomicReference(null)
    }

}