package com.zac.dlkit.sample.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zac.dlkit.presentation.Downloader
import com.zac.dlkit.sample.MainViewModel
import com.zac.dlkit.sample.R

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val downloader = Downloader.Builder(context).build()
        val mpdUrl = context.getString(R.string.test_clear_url)

        return MainViewModel(
            downloader,
            mpdUrl
        ) as T
    }

}