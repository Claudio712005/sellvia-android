package br.com.claus.sellvia.features.catalog.data

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import br.com.claus.sellvia.BuildConfig
import br.com.claus.sellvia.features.catalog.domain.model.CatalogDisplayOptions
import br.com.claus.sellvia.features.catalog.domain.model.CatalogFilterOptions
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CatalogDownloadManager(
    private val context: Context,
) {

    fun enqueue(
        companyId: Long,
        token: String,
        filter: CatalogFilterOptions,
        display: CatalogDisplayOptions,
    ): Long {
        val url = buildUrl(companyId, filter, display)
        val filename = "catalogo-$companyId-${
            LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
        }.pdf"

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            addRequestHeader("Authorization", "Bearer $token")
            setTitle("Catálogo de Produtos")
            setDescription("Baixando catálogo em PDF…")
            setMimeType("application/pdf")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(false)
        }

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return dm.enqueue(request)
    }

    private fun buildUrl(
        companyId: Long,
        filter: CatalogFilterOptions,
        display: CatalogDisplayOptions,
    ): String {
        val base = "${BuildConfig.BASE_URL}api/v1.0.0/companies/$companyId/catalog/pdf"
        val params = buildList {
            filter.name?.let { add("name=${it.trim()}") }
            filter.categoryId?.let { add("categoryId=$it") }
            filter.minPrice?.let { add("minPrice=$it") }
            filter.maxPrice?.let { add("maxPrice=$it") }
            filter.active?.let { add("active=$it") }
            filter.sku?.let { add("sku=${it.trim()}") }
            filter.type?.let { add("type=${it.name}") }
            filter.sort?.let { add("sort=$it") }
            filter.direction?.let { add("direction=${it.name}") }
            add("columnsPerRow=${display.columnsPerRow.coerceIn(1, 4)}")
            add("cardStyle=${display.cardStyle.name}")
            add("showProductImages=${display.showProductImages}")
            add("showCompanyLogo=${display.showCompanyLogo}")
            add("showCompanyBusinessName=${display.showCompanyBusinessName}")
            add("showCompanyCnpj=${display.showCompanyCnpj}")
            add("showCompanyWebsite=${display.showCompanyWebsite}")
            add("showStats=${display.showStats}")
            add("showAveragePrice=${display.showAveragePrice}")
            add("showActiveCount=${display.showActiveCount}")
            add("showTypeBreakdown=${display.showTypeBreakdown}")
            add("showSku=${display.showSku}")
            add("showStock=${display.showStock}")
            add("showProductionCost=${display.showProductionCost}")
            add("showCategory=${display.showCategory}")
            add("showDescription=${display.showDescription}")
            add("showStatus=${display.showStatus}")
        }
        return if (params.isEmpty()) base else "$base?${params.joinToString("&")}"
    }
}
