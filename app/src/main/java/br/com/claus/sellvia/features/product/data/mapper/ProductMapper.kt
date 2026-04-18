package br.com.claus.sellvia.features.product.data.mapper

import br.com.claus.sellvia.features.category.data.mapper.toDomain
import br.com.claus.sellvia.features.product.data.model.ProductResponse
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.features.product.domain.model.ProductType
import br.com.claus.sellvia.features.product.domain.model.ResourceStatus
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import java.math.BigDecimal

fun ProductResponse.toDomain(): Product = Product(
    id = id ?: 0L,
    name = name.orEmpty(),
    description = description.orEmpty(),
    price = price ?: BigDecimal.ZERO,
    productionCost = productionCost ?: BigDecimal.ZERO,
    companyId = companyId ?: 0L,
    status = status ?: ResourceStatus.ACTIVE,
    imageUrl = imageUrl,
    category = category?.toDomain(),
    sku = sku.orEmpty(),
    stockQuantity = stockQuantity,
    type = type ?: ProductType.PHYSICAL,
    createdAt = createdAt,
    updatedAt = updatedAt,
    createdBy = createdBy,
    updatedBy = updatedBy,
    externalLink = externalLink,
    whatsappMessage = whatsappMessage,
)

fun Pagination<ProductResponse>.toDomain(): Pagination<Product> = Pagination(
    items = items.orEmpty().map { it.toDomain() },
    currentPage = currentPage,
    perPage = perPage,
    totalPages = totalPages,
    totalItems = totalItems,
)
