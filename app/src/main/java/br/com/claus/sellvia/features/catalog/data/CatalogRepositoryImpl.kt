package br.com.claus.sellvia.features.catalog.data

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.catalog.domain.model.CatalogDisplayOptions
import br.com.claus.sellvia.features.catalog.domain.model.CatalogFilterOptions
import br.com.claus.sellvia.features.catalog.domain.repository.ICatalogRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CatalogRepositoryImpl(
    private val service: CatalogService,
    private val context: Context,
) : ICatalogRepository {

    override suspend fun downloadCatalog(
        companyId: Long,
        filter: CatalogFilterOptions,
        display: CatalogDisplayOptions,
    ): ResultWrapper<Uri> {
        return try {
            val response = service.downloadCatalog(
                companyId = companyId,
                name = filter.name,
                categoryId = filter.categoryId,
                minPrice = filter.minPrice,
                maxPrice = filter.maxPrice,
                active = filter.active,
                sku = filter.sku,
                type = filter.type?.name,
                sort = filter.sort,
                direction = filter.direction?.name,
                showProductImages = display.showProductImages,
                showCompanyLogo = display.showCompanyLogo,
                showCompanyBusinessName = display.showCompanyBusinessName,
                showCompanyCnpj = display.showCompanyCnpj,
                showCompanyWebsite = display.showCompanyWebsite,
                showStats = display.showStats,
                showAveragePrice = display.showAveragePrice,
                showActiveCount = display.showActiveCount,
                showTypeBreakdown = display.showTypeBreakdown,
                showSku = display.showSku,
                showStock = display.showStock,
                showProductionCost = display.showProductionCost,
                showCategory = display.showCategory,
                showDescription = display.showDescription,
                showStatus = display.showStatus,
            )

            if (!response.isSuccessful || response.body() == null) {
                return ResultWrapper.Error(
                    code = response.code(),
                    message = "Erro ao gerar catálogo (${response.code()})",
                )
            }

            val filename = "catalogo-$companyId-${
                LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
            }.pdf"

            val uri = withContext(Dispatchers.IO) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveViaMediaStore(response.body()!!, filename)
                } else {
                    saveToPublicDownloads(response.body()!!, filename)
                }
            }

            ResultWrapper.Success(uri)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ResultWrapper.Error(message = e.message ?: "Erro ao baixar catálogo")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveViaMediaStore(body: ResponseBody, filename: String): Uri {
        val resolver = context.contentResolver
        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val pending = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, filename)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val uri = resolver.insert(collection, pending)
            ?: throw IOException("Não foi possível criar a entrada no MediaStore")

        try {
            resolver.openOutputStream(uri)
                ?.use { out -> body.byteStream().use { it.copyTo(out) } }
                ?: throw IOException("Não foi possível abrir o stream de escrita")

            pending.clear()
            pending.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, pending, null, null)
        } catch (e: Exception) {
            resolver.delete(uri, null, null)
            throw e
        }

        return uri
    }

    private fun saveToPublicDownloads(body: ResponseBody, filename: String): Uri {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, filename)

        body.byteStream().use { input ->
            file.outputStream().use { input.copyTo(it) }
        }

        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }
}
