package br.com.claus.sellvia.features.catalog.domain.usecase

import android.net.Uri
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.catalog.domain.model.CatalogDisplayOptions
import br.com.claus.sellvia.features.catalog.domain.model.CatalogFilterOptions
import br.com.claus.sellvia.features.catalog.domain.repository.ICatalogRepository

class DownloadCatalogUseCase(private val repository: ICatalogRepository) {

    suspend operator fun invoke(
        companyId: Long,
        filter: CatalogFilterOptions = CatalogFilterOptions(),
        display: CatalogDisplayOptions = CatalogDisplayOptions(),
    ): ResultWrapper<Uri> = repository.downloadCatalog(
        companyId = companyId,
        filter = filter,
        display = display,
    )
}
