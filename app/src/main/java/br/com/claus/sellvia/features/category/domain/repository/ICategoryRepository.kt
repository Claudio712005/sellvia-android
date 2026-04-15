package br.com.claus.sellvia.features.category.domain.repository

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.model.Direction
import br.com.claus.sellvia.features.category.domain.model.Category
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination

interface ICategoryRepository {

    suspend fun getCategories(
        page: Int,
        perPage: Int,
        sort: String,
        sortDirection: Direction,
        companyId: Long?,
        name: String?
    ): ResultWrapper<Pagination<Category>>

    suspend fun createCategory(
        name: String,
        description: String,
        companyId: Long
    ): ResultWrapper<Unit>

    suspend fun updateCategory(
        id: Long,
        name: String,
        description: String,
        companyId: Long
    ): ResultWrapper<Category>

    suspend fun deleteCategory(id: Long): ResultWrapper<Unit>
}