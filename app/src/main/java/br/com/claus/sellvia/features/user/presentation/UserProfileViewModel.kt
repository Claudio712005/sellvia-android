package br.com.claus.sellvia.features.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.user.data.model.UpdatePasswordRequest
import br.com.claus.sellvia.features.user.domain.model.User
import br.com.claus.sellvia.features.user.domain.usecase.GetUserProfileUseCase
import br.com.claus.sellvia.features.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PasswordFieldErrors(
    val currentPassword: String? = null,
    val confirmPassword: String? = null,
    val newPassword: String? = null,
)

data class UserProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val showPasswordSheet: Boolean = false,
    val currentPassword: String = "",
    val confirmPassword: String = "",
    val newPassword: String = "",
    val passwordFieldErrors: PasswordFieldErrors = PasswordFieldErrors(),
    val isUpdatingPassword: Boolean = false,
    val passwordMessage: String? = null,
)

class UserProfileViewModel(
    private val tokenManager: TokenManager,
    private val getUserProfile: GetUserProfileUseCase,
    private val repository: IUserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val userId = tokenManager.userId().firstOrNull()
            if (userId == null) {
                _uiState.update { it.copy(isLoading = false, error = "Usuário não identificado") }
                return@launch
            }

            when (val result = getUserProfile(userId)) {
                is ResultWrapper.Success -> {
                    tokenManager.saveUserRole(result.data.role)
                    _uiState.update { it.copy(isLoading = false, user = result.data) }
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun onShowPasswordSheet() {
        _uiState.update {
            it.copy(
                showPasswordSheet = true,
                currentPassword = "",
                confirmPassword = "",
                newPassword = "",
                passwordFieldErrors = PasswordFieldErrors(),
                passwordMessage = null,
            )
        }
    }

    fun onDismissPasswordSheet() {
        _uiState.update { it.copy(showPasswordSheet = false) }
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                currentPassword = value,
                passwordFieldErrors = it.passwordFieldErrors.copy(currentPassword = null),
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmPassword = value,
                passwordFieldErrors = it.passwordFieldErrors.copy(confirmPassword = null),
            )
        }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                newPassword = value,
                passwordFieldErrors = it.passwordFieldErrors.copy(newPassword = null),
            )
        }
    }

    fun onSubmitPasswordUpdate() {
        if (!validatePasswordFields()) return

        viewModelScope.launch {
            val userId = tokenManager.userId().firstOrNull() ?: return@launch
            _uiState.update { it.copy(isUpdatingPassword = true) }

            val state = _uiState.value
            val request = UpdatePasswordRequest(
                password = state.currentPassword,
                confirmPassword = state.confirmPassword,
                newPassword = state.newPassword,
            )

            when (val result = repository.updatePassword(userId, request)) {
                is ResultWrapper.Success -> _uiState.update {
                    it.copy(
                        isUpdatingPassword = false,
                        showPasswordSheet = false,
                        passwordMessage = result.data.ifBlank { "Senha atualizada com sucesso" },
                    )
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(
                        isUpdatingPassword = false,
                        passwordMessage = result.message,
                    )
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun dismissPasswordMessage() {
        _uiState.update { it.copy(passwordMessage = null) }
    }

    private fun validatePasswordFields(): Boolean {
        val state = _uiState.value
        var errors = PasswordFieldErrors()
        var isValid = true

        if (state.currentPassword.isBlank()) {
            errors = errors.copy(currentPassword = "Senha atual obrigatória"); isValid = false
        }
        if (state.confirmPassword.isBlank()) {
            errors = errors.copy(confirmPassword = "Confirmação obrigatória"); isValid = false
        } else if (state.confirmPassword != state.currentPassword) {
            errors = errors.copy(confirmPassword = "Confirmação não confere com a senha atual"); isValid = false
        }
        if (state.newPassword.isBlank()) {
            errors = errors.copy(newPassword = "Nova senha obrigatória"); isValid = false
        } else if (state.newPassword.length < 6) {
            errors = errors.copy(newPassword = "Nova senha deve ter no mínimo 6 caracteres"); isValid = false
        }

        _uiState.update { it.copy(passwordFieldErrors = errors) }
        return isValid
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearTokens()
        }
    }
}
