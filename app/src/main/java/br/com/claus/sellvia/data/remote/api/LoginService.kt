package br.com.claus.sellvia.data.remote.api

import br.com.claus.sellvia.data.remote.model.request.LoginRequest
import br.com.claus.sellvia.data.remote.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}