package br.com.claus.sellvia.features.login.data

import br.com.claus.sellvia.features.login.data.model.LoginRequest
import br.com.claus.sellvia.features.login.data.model.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/refresh-token")
    fun refreshToken(@Header("Authorization") token: String): Call<LoginResponse>
}