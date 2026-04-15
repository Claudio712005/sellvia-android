package br.com.claus.sellvia.features.catalog.data

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.catalog.domain.model.CatalogDisplayOptions
import br.com.claus.sellvia.features.catalog.domain.model.CatalogFilterOptions
import br.com.claus.sellvia.features.catalog.domain.repository.ICatalogRepository

class CatalogRepositoryImpl(
    private val downloadManager: CatalogDownloadManager,
) : ICatalogRepository {

    override suspend fun downloadCatalog(
        companyId: Long,
        token: String,
        filter: CatalogFilterOptions,
        display: CatalogDisplayOptions,
    ): ResultWrapper<Long> {
        return try {
            val downloadId = downloadManager.enqueue(
                companyId = companyId,
                token = token,
                filter = filter,
                display = display,
            )
            ResultWrapper.Success(downloadId)
        } catch (e: Exception) {
            ResultWrapper.Error(message = e.message ?: "Erro ao iniciar download")
        }
    }
}
