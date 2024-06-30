package com.zac.dlkit.presentation.entity

data class DownloadConfig(
    val mpdUrl: String,
    val drmServerUrl: String? = null,
    val drmRequestHeader: Map<String, String>? = null
)
