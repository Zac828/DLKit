package com.zac.dlkit.data

import com.zac.dlkit.data.source.DownloadDataSource
import com.zac.dlkit.domain.DownloadRepository
import com.zac.dlkit.presentation.entity.DownloadConfig

class DownloadRepositoryImpl(
    private val exoDownloadDataSource: DownloadDataSource
) : DownloadRepository {

    override fun createDownloadHelper(downloadConfig: DownloadConfig) {
        exoDownloadDataSource.createDownloadHelper(downloadConfig)
    }

    override suspend fun prepareDownloadHelper(downloadConfig: DownloadConfig) =
        exoDownloadDataSource.prepareDownloadHelper(downloadConfig)

    override fun downloadDrmLicense(downloadConfig: DownloadConfig): List<Byte>? {
        return exoDownloadDataSource.downloadDrmLicense(downloadConfig)
    }

    override fun downloadContent(downloadConfig: DownloadConfig, keySet: List<Byte>?) {
        exoDownloadDataSource.downloadContent(downloadConfig, keySet)
    }

    override fun releaseDownloadHelper(downloadConfig: DownloadConfig) {
        exoDownloadDataSource.releaseDownloadHelper(downloadConfig)
    }

}