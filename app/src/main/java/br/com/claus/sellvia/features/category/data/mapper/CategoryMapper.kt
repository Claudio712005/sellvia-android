package br.com.claus.sellvia.features.category.data.mapper

import br.com.claus.sellvia.features.category.data.model.CategoryResponse
import br.com.claus.sellvia.features.category.domain.model.Category
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination

fun CategoryResponse.toDomain(): Category = Category(
    id = id ?: 0L,
    name = name.orEmpty(),
    description = description.orEmpty(),
    companyId = companyId ?: 0L,
    createdAt = createdAt,
    updatedAt = updatedAt,
    createdBy = createdBy,
    updatedBy = updatedBy
)

fun Pagination<CategoryResponse>.toDomain(): Pagination<Category> = Pagination(
    items = items.orEmpty().map { it.toDomain() },
    currentPage = currentPage,
    perPage = perPage,
    totalPages = totalPages,
    totalItems = totalItems
)