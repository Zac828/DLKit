package com.zac.dlkit.sample

import androidx.lifecycle.ViewModel
import com.zac.dlkit.presentation.Downloader
import com.zac.dlkit.presentation.entity.DownloadConfig

class MainViewModel(
    private val downloader: Downloader,
    private val mpdUrl: String
) : ViewModel() {

    fun startDownload() {
        val config = DownloadConfig(
            mpdUrl = mpdUrl
        )
        downloader.download(config)
    }

}