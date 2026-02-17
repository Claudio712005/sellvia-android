package br.com.claus.sellvia.features.listCategory.data

import br.com.claus.sellvia.data.enums.Direction
import br.com.claus.sellvia.data.remote.api.CategoryService

class ListCategoryRepository(
    private val api: CategoryService
) {

    suspend fun buscarPaginado(
        token: String?,
        page: Int,
        perPage: Int,
        sort: String,
        sortDirection: Direction,
        companyId: Long?
    ) = api.getCategories(
        token = "Bearer $token",
        page = page,
        pageSize = perPage,
        sortBy = sort,
        sortDirection = sortDirection.name,
        companyId = companyId
    )
}