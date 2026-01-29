package br.com.claus.sellvia.features.login.data.repository

import br.com.claus.sellvia.features.login.data.remote.LoginApi
import br.com.claus.sellvia.features.login.data.remote.dto.LoginRequest
import br.com.claus.sellvia.features.login.domain.repository.LoginRepository
import br.com.claus.sellvia.features.login.domain.model.User

class LoginRepositoryImpl(
    private val api: LoginApi
): LoginRepository {

    override suspend fun login(email: String, password: String): User {
        val response = api.login(LoginRequest(email, password))
        return User(
            id = response.id,
            name = response.name,
            email = response.email
        )
    }
}