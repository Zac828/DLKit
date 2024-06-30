package com.zac.dlkit.presentation

import android.content.Context
import com.zac.dlkit.domain.di.DownloadRepositoryInject
import com.zac.dlkit.domain.usecase.DownloadContentUseCase
import com.zac.dlkit.domain.usecase.DownloadDrmLicenseUseCase
import com.zac.dlkit.domain.usecase.MainDownloadUseCase
import com.zac.dlkit.presentation.internal.DownloaderImpl
import com.zac.dlkit.presentation.entity.DownloadConfig

interface Downloader {

    companion object {
        internal val downloader: Downloader? = null
    }

    class Builder(context: Context) {

        private val appContext = context.applicationContext

        fun build(): Downloader {
            val downloadRepository = DownloadRepositoryInject.provideDownloadRepository(appContext)

            val downloadDrmLicenseUseCase = DownloadDrmLicenseUseCase(downloadRepository)
            val downloadContentUseCase = DownloadContentUseCase(downloadRepository)
            val mainDownloadUseCase = MainDownloadUseCase(
                downloadRepository,
                downloadDrmLicenseUseCase,
                downloadContentUseCase
            )

            return downloader ?: DownloaderImpl(
                downloadUseCase = mainDownloadUseCase
            )
        }

    }

    /**
     * Start a download task
     *
     * @param downloadConfig Description of the download content information
     */
    fun download(downloadConfig: DownloadConfig)

}