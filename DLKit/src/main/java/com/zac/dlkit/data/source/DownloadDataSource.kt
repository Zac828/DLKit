package com.zac.dlkit.data.source

import com.zac.dlkit.presentation.entity.DownloadConfig
import kotlinx.coroutines.flow.Flow

interface DownloadDataSource {

    fun createDownloadHelper(downloadConfig: DownloadConfig)

    suspend fun prepareDownloadHelper(downloadConfig: DownloadConfig): Flow<Result<Unit>>

    fun downloadDrmLicense(downloadConfig: DownloadConfig): List<Byte>?

    fun downloadContent(downloadConfig: DownloadConfig, keySet: List<Byte>?)

    fun releaseDownloadHelper(downloadConfig: DownloadConfig)

}