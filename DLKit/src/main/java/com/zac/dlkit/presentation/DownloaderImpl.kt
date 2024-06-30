package com.zac.dlkit.presentation

import android.util.Log
import com.zac.dlkit.Downloader
import com.zac.dlkit.domain.usecase.MainDownloadUseCase
import com.zac.dlkit.presentation.entity.DownloadConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DownloaderImpl(
    private val downloadUseCase: MainDownloadUseCase,
    private val mainScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) : Downloader {

    override fun download(downloadConfig: DownloadConfig) {
        mainScope.launch {
            downloadUseCase(
                MainDownloadUseCase.Param(
                    downloadConfig,
                    ::onError
                )
            )
        }
    }

    private fun onError(e: Exception) {
        Log.e(TAG, "Error: $e")
    }

    companion object {
        private const val TAG = "DLKit"
    }
}