package br.com.claus.sellvia.features.product.data.mapper

import br.com.claus.sellvia.features.product.data.model.ProductResponse
import br.com.claus.sellvia.features.category.data.mapper.toDomain
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination

fun ProductResponse.toDomain(): Product = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    productionCost = productionCost,
    companyId = companyId,
    status = status,
    imageUrl = imageUrl,
    category = category?.toDomain(),
    sku = sku,
    stockQuantity = stockQuantity,
    type = type,
    createdAt = createdAt,
    updatedAt = updatedAt,
    createdBy = createdBy,
    updatedBy = updatedBy
)

fun Pagination<ProductResponse>.toDomain(): Pagination<Product> = Pagination(
    items = items.map { it.toDomain() },
    currentPage = currentPage,
    perPage = perPage,
    totalPages = totalPages,
    totalItems = totalItems
)