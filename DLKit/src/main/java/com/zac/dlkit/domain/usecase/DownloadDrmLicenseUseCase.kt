package com.zac.dlkit.domain.usecase

import com.zac.dlkit.domain.DownloadRepository
import com.zac.dlkit.presentation.entity.DownloadConfig
import kotlinx.coroutines.flow.first

class DownloadDrmLicenseUseCase(
    private val downloadRepository: DownloadRepository
) {

    suspend operator fun invoke(param: Param): List<Byte>? {
        downloadRepository.prepareDownloadHelper(param.downloadConfig).first()
        return downloadRepository.downloadDrmLicense(param.downloadConfig)
    }

    data class Param(
        val downloadConfig: DownloadConfig
    )

}