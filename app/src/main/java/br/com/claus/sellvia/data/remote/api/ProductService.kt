package br.com.claus.sellvia.data.remote.api

import br.com.claus.sellvia.data.enums.Direction
import br.com.claus.sellvia.data.remote.model.response.ProductResponse
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductService {

    @GET(value = "products")
    suspend fun findAll(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("perPage") perPage: Int?,
        @Query("sort") sort: String?,
        @Query("direction") direction: Direction? = Direction.DESC,
        @Query("companyId") companyId: Long?,
        @Query("name") name: String?,
        @Query("categoryId") categoryId: Long?,
        @Query("minPrice") minPrice: Double?,
        @Query("maxPrice") maxPrice: Double?,
        @Query("minCreatedAt") minCreatedAt: String?,
        @Query("maxCreatedAt") maxCreatedAt: String?,
        @Query("minUpdatedAt") minUpdatedAt: String?,
        @Query("maxUpdatedAt") maxUpdatedAt: String?,
        @Query("active") active: Boolean?,
        @Query("createdBy") createdBy: String?,
        @Query("updatedBy") updatedBy: String?,
        @Query("id") id: Long?,
        @Query("sku") sku: String?
    ): Pagination<ProductResponse>

}