package br.com.claus.sellvia.features.listCategory.data

import br.com.claus.sellvia.data.enums.Direction
import br.com.claus.sellvia.data.remote.api.CategoryService
import br.com.claus.sellvia.data.remote.model.request.CategoryRequest

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

    suspend fun registry(
        request: CategoryRequest,
        token: String?
    ) = api.createCategory(
        request = request,
        token = "Bearer $token"
    )

    suspend fun delete(
        id: Long,
        token: String?
    ) = api.deleteCategory(
        id = id,
        token = "Bearer $token"
    )

    suspend fun update(
        id: Long,
        request: CategoryRequest,
        token: String?
    ) = api.updateCategory(
        id = id,
        request = request, token = "Bearer $token"
    )
}

