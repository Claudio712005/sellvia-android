package br.com.claus.sellvia.features.product.data

import br.com.claus.sellvia.core.model.Direction
import br.com.claus.sellvia.features.product.data.model.ProductRequest
import br.com.claus.sellvia.features.product.data.model.ProductResponse
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET("products")
    suspend fun findAll(
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

    @Multipart
    @POST("products")
    suspend fun create(
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @PUT("products/{id}")
    suspend fun update(
        @Body request: ProductRequest,
        @Path("id") id: Long
    ): Response<ProductResponse>

    @Multipart
    @PUT("products/{id}/update-image")
    suspend fun updateImage(
        @Path("id") id: Long,
        @Part image: MultipartBody.Part
    ): Response<ProductResponse>

    @DELETE("products/{id}")
    suspend fun delete(
        @Path("id") id: Long
    ): Response<Unit>
}