package com.zac.dlkit.sample

import androidx.lifecycle.ViewModel
import com.zac.dlkit.Downloader
import com.zac.dlkit.presentation.entity.DownloadConfig

class MainViewModel(
    private val downloader: Downloader
) : ViewModel() {

    fun startDownload() {
        val config = DownloadConfig(
            mpdUrl = TEST_CLEAR_URL
        )
        downloader.download(config)
    }

    companion object {
        private const val TEST_CLEAR_URL = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears_hd.mpd"
    }

}