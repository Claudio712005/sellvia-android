package br.com.claus.sellvia.features.login.presentation

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val startAnimation: Boolean = false,
    val showLoginForm: Boolean = false
)