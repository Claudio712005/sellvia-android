package br.com.claus.sellvia.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.features.login.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = androidx.compose.runtime.mutableStateOf(LoginUiState())
    val uiState = _uiState

    fun onEvent(event: LoginEvent) {
        when (event) {

            LoginEvent.Start ->
                _uiState.value = _uiState.value.copy(startAnimation = true)

            LoginEvent.ClickEnter ->
                _uiState.value = _uiState.value.copy(showLoginForm = true)

            LoginEvent.ClickBack ->
                _uiState.value = _uiState.value.copy(showLoginForm = false)

            is LoginEvent.EmailChanged ->
                _uiState.value = _uiState.value.copy(email = event.value)

            is LoginEvent.PasswordChanged ->
                _uiState.value = _uiState.value.copy(password = event.value)

            LoginEvent.SubmitLogin ->
                login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            runCatching {
                loginUseCase(
                    _uiState.value.email,
                    _uiState.value.password
                )
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }
}
