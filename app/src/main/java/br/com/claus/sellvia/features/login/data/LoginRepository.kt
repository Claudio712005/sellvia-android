package br.com.claus.sellvia.features.login.data

import br.com.claus.sellvia.data.remote.api.LoginService
import br.com.claus.sellvia.data.remote.model.request.LoginRequest

class LoginRepository(private val api: LoginService){
    suspend fun login(username: String, password: String) =
        api.login(LoginRequest(
            username, password
        ))
}