package br.com.claus.sellvia.features.listProduct.data

import br.com.claus.sellvia.data.remote.api.ProductService
import br.com.claus.sellvia.data.remote.model.request.ProductSearchQuery

class ListProductRepository(
    private val api: ProductService
) {

    suspend fun findAll(
        query: ProductSearchQuery,
        token: String?
    ) = api.findAll(
        token = "Bearer $token",
        page = query.page,
        perPage = query.perPage,
        sort = query.sort,
        direction = query.direction,
        companyId = query.companyId,
        name = query.name,
        categoryId = query.categoryId,
        minPrice = query.minPrice,
        maxPrice = query.maxPrice,
        minCreatedAt = query.minCreatedAt,
        maxCreatedAt = query.maxCreatedAt,
        minUpdatedAt = query.minUpdatedAt,
        maxUpdatedAt = query.maxUpdatedAt,
        active = query.active,
        createdBy = query.createdBy,
        updatedBy = query.updatedBy,
        id = query.id,
        sku = query.sku
    )
}