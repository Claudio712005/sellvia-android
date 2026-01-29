package br.com.claus.sellvia.features.login.data.remote.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val id: Long,
    val name: String,
    val email: String
)