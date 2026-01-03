package com.zac.dlkit.presentation.internal

import com.zac.dlkit.domain.usecase.MainDownloadUseCase
import com.zac.dlkit.presentation.entity.DownloadConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class DownloaderImplTest {

    private val downloadUseCase: MainDownloadUseCase = mock()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var downloader: DownloaderImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        downloader = DownloaderImpl(downloadUseCase, mainScope = kotlinx.coroutines.CoroutineScope(testDispatcher))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `download calls downloadUseCase with correct config`() = runTest(testDispatcher) {
        val config = DownloadConfig(mpdUrl = "test_url")

        downloader.download(config)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(downloadUseCase).invoke(any())
    }
}