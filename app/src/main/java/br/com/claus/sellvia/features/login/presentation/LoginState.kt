package br.com.claus.sellvia.features.login.presentation

data class LoginState(
    val email: String = "",
    val password: String = "",
    val step: Int = 1
)