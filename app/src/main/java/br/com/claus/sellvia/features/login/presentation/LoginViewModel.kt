package br.com.claus.sellvia.features.login.presentation

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.features.login.data.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var rememberMe by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun onLoginClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = repository.login(username, password)

                if (response.isSuccessful && response.body() != null) {
                    val loginData = response.body()!!

                    tokenManager.saveTokens(
                        token = loginData.token,
                        refreshToken = loginData.refreshToken,
                        companyId = loginData.company.id,
                        userId = loginData.user.id,
                        role = loginData.user.role
                    )
                    tokenManager.saveKeepLoggedIn(rememberMe)

                    onSuccess()
                } else {
                    errorMessage = "Usuário ou senha inválidos"
                }
            } catch (e: Exception) {
                errorMessage = "Erro de conexão: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
