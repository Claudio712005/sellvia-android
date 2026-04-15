package br.com.claus.sellvia.features.catalog.domain.repository

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.catalog.domain.model.CatalogDisplayOptions
import br.com.claus.sellvia.features.catalog.domain.model.CatalogFilterOptions

interface ICatalogRepository {

    suspend fun downloadCatalog(
        companyId: Long,
        token: String,
        filter: CatalogFilterOptions,
        display: CatalogDisplayOptions,
    ): ResultWrapper<Long>
}
