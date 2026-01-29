package br.com.claus.sellvia.features.login.domain.repository

import br.com.claus.sellvia.features.login.domain.model.User

interface LoginRepository {
    suspend fun login(email: String, password: String): User
}