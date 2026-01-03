package com.zac.dlkit.domain.usecase

import com.zac.dlkit.domain.DownloadRepository
import com.zac.dlkit.presentation.entity.DownloadConfig
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DownloadDrmLicenseUseCaseTest {

    private val downloadRepository: DownloadRepository = mock()
    private val useCase = DownloadDrmLicenseUseCase(downloadRepository)

    @Test
    fun `invoke calls repository prepare and download license`() = runTest {
        val config = DownloadConfig(mpdUrl = "test_url")
        val expectedLicense = listOf<Byte>(1, 2, 3)

        whenever(downloadRepository.prepareDownloadHelper(config)).thenReturn(flowOf(Result.success(Unit)))
        whenever(downloadRepository.downloadDrmLicense(config)).thenReturn(expectedLicense)

        val result = useCase(DownloadDrmLicenseUseCase.Param(config))

        verify(downloadRepository).prepareDownloadHelper(config)
        verify(downloadRepository).downloadDrmLicense(config)
        assertEquals(expectedLicense, result)
    }
}