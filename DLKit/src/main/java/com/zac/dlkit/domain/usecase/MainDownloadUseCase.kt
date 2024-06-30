package com.zac.dlkit.domain.usecase

import com.zac.dlkit.domain.DownloadRepository
import com.zac.dlkit.presentation.entity.DownloadConfig

class MainDownloadUseCase(
    private val downloadRepository: DownloadRepository,
    private val downloadDrmLicenseUseCase: DownloadDrmLicenseUseCase,
    private val downloadContentUseCase: DownloadContentUseCase
) {

    suspend operator fun invoke(param: Param) {
        try {
            downloadRepository.createDownloadHelper(param.downloadConfig)
        } catch (e: Exception) {
            param.onError(e)
        }

        val keySet = downloadDrmLicenseUseCase(
            DownloadDrmLicenseUseCase.Param(
                param.downloadConfig
            )
        )

        downloadContentUseCase(
            DownloadContentUseCase.Param(
                param.downloadConfig,
                keySet
            )
        )
    }

    data class Param(
        val downloadConfig: DownloadConfig,
        val onError: (e: Exception) -> Unit
    )

}