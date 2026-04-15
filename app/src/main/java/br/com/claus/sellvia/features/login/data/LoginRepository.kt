package br.com.claus.sellvia.features.login.data

import br.com.claus.sellvia.features.login.data.LoginService
import br.com.claus.sellvia.features.login.data.model.LoginRequest

class LoginRepository(private val api: LoginService){
    suspend fun login(username: String, password: String) =
        api.login(LoginRequest(
            username, password
        ))
}