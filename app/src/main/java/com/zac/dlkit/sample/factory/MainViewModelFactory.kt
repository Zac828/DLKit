package com.zac.dlkit.sample.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zac.dlkit.Downloader
import com.zac.dlkit.sample.MainViewModel

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val downloader = Downloader.Builder(context).build()

        return MainViewModel(
            downloader
        ) as T
    }

}