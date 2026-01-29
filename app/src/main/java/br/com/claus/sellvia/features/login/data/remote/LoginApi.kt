package br.com.claus.sellvia.features.login.data.remote

import br.com.claus.sellvia.features.login.data.remote.dto.LoginRequest
import br.com.claus.sellvia.features.login.data.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
}