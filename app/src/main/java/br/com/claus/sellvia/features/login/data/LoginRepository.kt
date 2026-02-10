package br.com.claus.sellvia.features.login.data

import br.com.claus.sellvia.data.remote.api.LoginService
import br.com.claus.sellvia.data.remote.model.request.LoginRequest

class LoginRepository(private val api: LoginService){
    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(
            email, password
        ))
}