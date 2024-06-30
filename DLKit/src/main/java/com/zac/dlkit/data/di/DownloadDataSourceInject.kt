package com.zac.dlkit.data.di

import android.content.Context
import android.os.Build
import com.google.android.exoplayer2.ExoPlayerLibraryInfo
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.zac.dlkit.data.source.DownloadDataSource
import com.zac.dlkit.data.source.DownloadTracker
import com.zac.dlkit.data.source.ExoDownloadDataSource
import com.zac.dlkit.data.source.ExoDownloadTracker
import java.io.File
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executors

object DownloadDataSourceInject {

    private const val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
    private val USER_AGENT = ("DLKit/"
            + ExoPlayerLibraryInfo.VERSION
            + " (Linux; Android "
            + Build.VERSION.RELEASE
            + ") ")
    const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"

    fun provideExoDownloadDataSource(context: Context): DownloadDataSource {
        val downloadManager = provideDownloadManager(context)
        return ExoDownloadDataSource(
            context,
            downloadManager,
            provideDownloadTracker(downloadManager),
            getHttpDataSourceFactory()
        )
    }

    private fun provideDownloadTracker(
        downloadManager: DownloadManager
    ): DownloadTracker {
        return ExoDownloadTracker(downloadManager)
    }

    private fun provideDownloadManager(context: Context): DownloadManager {
        return DownloadManager(
            context,
            provideDataBaseProvider(context),
            provideDownloadCache(context),
            getHttpDataSourceFactory(),
            Executors.newFixedThreadPool(6)
        )
    }

    private fun provideDataBaseProvider(context: Context): DatabaseProvider {
        return ExoDatabaseProvider(context)
    }

    private fun provideDownloadCache(context: Context): Cache {
        val downloadContentDirectory =
            File(getDownloadDirectory(context), DOWNLOAD_CONTENT_DIRECTORY)
        return SimpleCache(
            downloadContentDirectory,
            NoOpCacheEvictor(),
            provideDataBaseProvider(context)
        )
    }

    private fun getDownloadDirectory(context: Context): File {
        return context.getExternalFilesDir(null) ?: context.filesDir
    }

    private fun getHttpDataSourceFactory(): HttpDataSource.Factory {
        val cookieManager = CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        }
        CookieManager.setDefault(cookieManager)
        return DefaultHttpDataSource.Factory().setUserAgent(USER_AGENT)
    }

}