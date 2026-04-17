package br.com.claus.sellvia.features.catalog.domain.repository

import android.net.Uri
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.catalog.domain.model.CatalogDisplayOptions
import br.com.claus.sellvia.features.catalog.domain.model.CatalogFilterOptions

interface ICatalogRepository {

    suspend fun downloadCatalog(
        companyId: Long,
        filter: CatalogFilterOptions,
        display: CatalogDisplayOptions,
    ): ResultWrapper<Uri>
}
