package br.com.claus.sellvia.features.catalog.data

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

class CatalogDownloadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return

        val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
        if (downloadId == -1L) return

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = dm.query(query) ?: return

        if (!cursor.moveToFirst()) {
            cursor.close()
            return
        }

        val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val localUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)

        val status = cursor.getInt(statusIndex)
        val localUri = cursor.getString(localUriIndex)
        cursor.close()

        if (status != DownloadManager.STATUS_SUCCESSFUL || localUri == null) return

        openPdf(context, localUri)
    }

    private fun openPdf(context: Context, localUri: String) {
        val uri: Uri = try {
            val file = File(Uri.parse(localUri).path ?: return)
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file,
            )
        } catch (e: Exception) {
            Uri.parse(localUri)
        }

        val openIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooser = Intent.createChooser(openIntent, "Abrir catálogo PDF").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (openIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooser)
        } else {
            Toast.makeText(
                context,
                "Nenhum aplicativo encontrado para abrir PDF. Acesse em Downloads.",
                Toast.LENGTH_LONG,
            ).show()
        }
    }
}
