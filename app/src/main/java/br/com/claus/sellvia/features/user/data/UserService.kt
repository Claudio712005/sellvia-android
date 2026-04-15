package br.com.claus.sellvia.features.user.data

import br.com.claus.sellvia.features.user.data.model.UpdatePasswordRequest
import br.com.claus.sellvia.features.user.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    @GET("users/{id}")
    suspend fun findById(@Path("id") id: Long): UserResponse

    @PUT("users/password/{id}")
    suspend fun updatePassword(
        @Path("id") id: Long,
        @Body request: UpdatePasswordRequest,
    ): Response<String>
}
