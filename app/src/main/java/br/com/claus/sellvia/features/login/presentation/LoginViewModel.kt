package br.com.claus.sellvia.features.login.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.OnEmailChange -> {
                _state.value = _state.value.copy(email = event.value)
            }

            is LoginEvent.OnPasswordChange -> {
                _state.value = _state.value.copy(password = event.value)
            }

            LoginEvent.OnLoginClick -> {
            }
        }
    }
}