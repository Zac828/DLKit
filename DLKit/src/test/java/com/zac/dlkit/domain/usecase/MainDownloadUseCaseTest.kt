package com.zac.dlkit.domain.usecase

import com.zac.dlkit.domain.DownloadRepository
import com.zac.dlkit.presentation.entity.DownloadConfig
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MainDownloadUseCaseTest {

    private val downloadRepository: DownloadRepository = mock()
    private val downloadDrmLicenseUseCase: DownloadDrmLicenseUseCase = mock()
    private val downloadContentUseCase: DownloadContentUseCase = mock()
    private val useCase = MainDownloadUseCase(
        downloadRepository,
        downloadDrmLicenseUseCase,
        downloadContentUseCase
    )

    @Test
    fun `invoke calls repository and other use cases correctly`() = runTest {
        val config = DownloadConfig(mpdUrl = "test_url")
        val onError: (Exception) -> Unit = mock()
        val keySet = listOf<Byte>(1, 2)

        whenever(downloadDrmLicenseUseCase.invoke(any())).thenReturn(keySet)

        useCase(MainDownloadUseCase.Param(config, onError))

        verify(downloadRepository).createDownloadHelper(config)
        verify(downloadDrmLicenseUseCase).invoke(DownloadDrmLicenseUseCase.Param(config))
        verify(downloadContentUseCase).invoke(DownloadContentUseCase.Param(config, keySet))
    }
}