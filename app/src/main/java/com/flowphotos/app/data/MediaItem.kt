package com.flowphotos.app.data

import android.net.Uri

data class MediaItem(
    val id: Long,
    val uri: Uri,
    val displayName: String,
    val dateTaken: Long,
    val isVideo: Boolean,
    val durationMs: Long = 0L,
)
