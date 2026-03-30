package br.com.claus.sellvia.data.remote.model.request

import br.com.claus.sellvia.data.enums.Direction

data class ProductSearchQuery(
    val page: Int = 0,
    val perPage: Int = 10,
    val sort: String = "id",
    val direction: Direction? = Direction.DESC,
    val companyId: Long? = null,
    val name: String? = null,
    val categoryId: Long? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minCreatedAt: String? = null,
    val maxCreatedAt: String? = null,
    val minUpdatedAt: String? = null,
    val maxUpdatedAt: String? = null,
    val active: Boolean? = null,
    val createdBy: String? = null,
    val updatedBy: String? = null,
    val id: Long? = null,
    val sku: String? = null
)