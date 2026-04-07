package br.com.claus.sellvia.features.category.data

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.network.mapSuccess
import br.com.claus.sellvia.core.network.safeApiCall
import br.com.claus.sellvia.core.network.safeDirectCall
import br.com.claus.sellvia.core.model.Direction
import br.com.claus.sellvia.features.category.data.CategoryService
import br.com.claus.sellvia.features.category.data.model.CategoryRequest
import br.com.claus.sellvia.features.category.data.mapper.toDomain
import br.com.claus.sellvia.features.category.domain.model.Category
import br.com.claus.sellvia.features.category.domain.repository.ICategoryRepository
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination

class CategoryRepositoryImpl(
    private val service: CategoryService
) : ICategoryRepository {

    override suspend fun getCategories(
        page: Int,
        perPage: Int,
        sort: String,
        sortDirection: Direction,
        companyId: Long?,
        name: String?
    ): ResultWrapper<Pagination<Category>> =
        safeDirectCall {
            service.getCategories(
                page = page,
                pageSize = perPage,
                sortBy = sort,
                sortDirection = sortDirection.name,
                companyId = companyId,
                name = name
            )
        }.
            mapSuccess { it.toDomain() }

    override suspend fun createCategory(
        name: String,
        description: String,
        companyId: Long
    ): ResultWrapper<Unit> =
        safeApiCall {
            service.createCategory(
                CategoryRequest(name = name, description = description, companyId = companyId)
            )
        }

    override suspend fun updateCategory(
        id: Long,
        name: String,
        description: String,
        companyId: Long
    ): ResultWrapper<Category> =
        safeApiCall {
            service.updateCategory(
                id = id,
                request = CategoryRequest(name = name, description = description, companyId = companyId)
            )
        }.mapSuccess { it.toDomain() }

    override suspend fun deleteCategory(id: Long): ResultWrapper<Unit> =
        safeApiCall { service.deleteCategory(id) }
}