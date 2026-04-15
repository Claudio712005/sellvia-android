package br.com.claus.sellvia.features.catalog.domain.model

import br.com.claus.sellvia.core.model.Direction
import br.com.claus.sellvia.features.product.domain.model.ProductType

data class CatalogFilterOptions(
    val name: String? = null,
    val categoryId: Long? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val active: Boolean? = null,
    val sku: String? = null,
    val type: ProductType? = null,
    val sort: String? = null,
    val direction: Direction? = null,
)
