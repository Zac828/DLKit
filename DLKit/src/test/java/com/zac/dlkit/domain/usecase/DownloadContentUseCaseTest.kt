package com.zac.dlkit.domain.usecase

import com.zac.dlkit.domain.DownloadRepository
import com.zac.dlkit.presentation.entity.DownloadConfig
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DownloadContentUseCaseTest {

    private val downloadRepository: DownloadRepository = mock()
    private val useCase = DownloadContentUseCase(downloadRepository)

    @Test
    fun `invoke calls downloadRepository with correct parameters`() {
        val config = DownloadConfig(mpdUrl = "test_url")
        val keySet = listOf<Byte>(1, 2, 3)
        val param = DownloadContentUseCase.Param(config, keySet)

        useCase(param)

        verify(downloadRepository).downloadContent(config, keySet)
    }
}