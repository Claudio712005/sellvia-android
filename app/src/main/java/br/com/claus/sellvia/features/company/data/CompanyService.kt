package br.com.claus.sellvia.features.company.data

import br.com.claus.sellvia.features.company.data.model.CompanyRequest
import br.com.claus.sellvia.features.company.data.model.CompanyResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface CompanyService {

    @GET("companies/{id}")
    suspend fun findById(@Path("id") id: Long): CompanyResponse

    @PUT("companies/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body request: CompanyRequest,
    ): Response<CompanyResponse>

    @Multipart
    @PUT("companies/{id}/update-image")
    suspend fun updateImage(
        @Path("id") id: Long,
        @Part image: MultipartBody.Part,
    ): Response<CompanyResponse>
}
