package br.com.claus.sellvia.features.login.presentation

sealed interface LoginEvent {
    data class OnEmailChange(val value: String): LoginEvent
    data class OnPasswordChange(val value: String) : LoginEvent
    object OnLoginClick : LoginEvent
}