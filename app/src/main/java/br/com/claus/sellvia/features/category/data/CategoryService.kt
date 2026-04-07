package br.com.claus.sellvia.features.category.data

import br.com.claus.sellvia.features.category.data.model.CategoryRequest
import br.com.claus.sellvia.features.category.data.model.CategoryResponse
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import retrofit2.Response
import retrofit2.http.*

interface CategoryService {

    @GET("categories")
    suspend fun getCategories(
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 10,
        @Query("sortBy") sortBy: String = "id",
        @Query("sortDirection") sortDirection: String = "ASC",
        @Query("companyId") companyId: Long?,
        @Query("name") name: String?
    ): Pagination<CategoryResponse>

    @POST("categories")
    suspend fun createCategory(
        @Body request: CategoryRequest
    ): Response<Unit>

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Long,
        @Body request: CategoryRequest
    ): Response<CategoryResponse>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Long
    ): Response<Unit>
}