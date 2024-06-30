package com.zac.dlkit.data.service

import android.app.Notification
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Scheduler
import com.zac.dlkit.R
import com.zac.dlkit.data.di.DownloadDataSourceInject
import com.zac.dlkit.data.source.ExoDownloadDataSource

class DLDownloadService : DownloadService(
    1,
    1000,
    DownloadDataSourceInject.DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    R.string.channel_name,
    0
) {

    override fun getDownloadManager(): DownloadManager {
        return ExoDownloadDataSource.downloadManagerAtom.get()
    }

    override fun getScheduler(): Scheduler {
        return PlatformScheduler(this, JOB_ID)
    }

    override fun getForegroundNotification(downloads: MutableList<Download>): Notification {
        return ExoDownloadDataSource.downloadNotificationHelperAtom.get().buildProgressNotification(
            this, R.drawable.ic_download, null, null, downloads
        )
    }

    companion object {
        private const val JOB_ID = 1

        private const val FOREGROUND_NOTIFICATION_ID = 1
    }

}