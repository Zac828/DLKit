package com.zac.dlkit.domain.di

import android.content.Context
import com.zac.dlkit.data.DownloadRepositoryImpl
import com.zac.dlkit.data.di.DownloadDataSourceInject
import com.zac.dlkit.domain.DownloadRepository

object DownloadRepositoryInject {

    fun provideDownloadRepository(context: Context): DownloadRepository {
        val dataSource = DownloadDataSourceInject.provideExoDownloadDataSource(context)
        return DownloadRepositoryImpl(
            dataSource
        )
    }

}