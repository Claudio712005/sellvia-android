package br.com.claus.sellvia.features.login.presentation

sealed class LoginEvent {
    data object Start : LoginEvent()
    data object ClickEnter : LoginEvent()
    data object ClickBack : LoginEvent()
    data class EmailChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    data object SubmitLogin : LoginEvent()
}
