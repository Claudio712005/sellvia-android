package br.com.claus.sellvia.features.catalog.presentation

import android.net.Uri

sealed interface DownloadStatus {
    data object Idle : DownloadStatus
    data object InProgress : DownloadStatus
    data class Success(val uri: Uri) : DownloadStatus
    data class Error(val message: String) : DownloadStatus
}
