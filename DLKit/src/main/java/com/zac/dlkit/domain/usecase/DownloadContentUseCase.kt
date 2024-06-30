package com.zac.dlkit.domain.usecase

import com.zac.dlkit.domain.DownloadRepository
import com.zac.dlkit.presentation.entity.DownloadConfig

class DownloadContentUseCase(
    private val downloadRepository: DownloadRepository
) {

    operator fun invoke(param: Param) {
        downloadRepository.downloadContent(
            param.downloadConfig,
            param.keySet
        )
    }

    data class Param(
        val downloadConfig: DownloadConfig,
        val keySet: List<Byte>?
    )

}