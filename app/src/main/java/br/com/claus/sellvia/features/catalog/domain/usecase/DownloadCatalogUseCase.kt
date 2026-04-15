package br.com.claus.sellvia.features.catalog.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.catalog.domain.model.CatalogDisplayOptions
import br.com.claus.sellvia.features.catalog.domain.model.CatalogFilterOptions
import br.com.claus.sellvia.features.catalog.domain.repository.ICatalogRepository

class DownloadCatalogUseCase(private val repository: ICatalogRepository) {

    suspend operator fun invoke(
        companyId: Long,
        token: String,
        filter: CatalogFilterOptions = CatalogFilterOptions(),
        display: CatalogDisplayOptions = CatalogDisplayOptions(),
    ): ResultWrapper<Long> = repository.downloadCatalog(
        companyId = companyId,
        token = token,
        filter = filter,
        display = display,
    )
}
