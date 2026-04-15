package br.com.claus.sellvia.features.company.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.company.data.model.CompanyRequest
import br.com.claus.sellvia.features.company.domain.model.Company
import br.com.claus.sellvia.features.company.domain.usecase.GetCompanyUseCase
import br.com.claus.sellvia.features.company.domain.usecase.UpdateCompanyImageUseCase
import br.com.claus.sellvia.features.company.domain.usecase.UpdateCompanyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CompanyFieldErrors(
    val name: String? = null,
    val phone: String? = null,
)

data class CompanyUiState(
    val isLoading: Boolean = false,
    val company: Company? = null,
    val error: String? = null,
    val name: String = "",
    val websiteUrl: String = "",
    val mainPhoneNumber: String = "",
    val isActive: Boolean = true,
    val fieldErrors: CompanyFieldErrors = CompanyFieldErrors(),
    val isSaving: Boolean = false,
    val saveMessage: String? = null,
    val pendingImageUri: Uri? = null,
    val isUploadingImage: Boolean = false,
    val imageMessage: String? = null,
)

class CompanyViewModel(
    private val tokenManager: TokenManager,
    private val getCompany: GetCompanyUseCase,
    private val updateCompany: UpdateCompanyUseCase,
    private val updateCompanyImage: UpdateCompanyImageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyUiState())
    val uiState: StateFlow<CompanyUiState> = _uiState.asStateFlow()

    init {
        loadCompany()
    }

    fun loadCompany() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val companyId = tokenManager.companyId().firstOrNull()
            if (companyId == null) {
                _uiState.update { it.copy(isLoading = false, error = "Empresa não identificada") }
                return@launch
            }

            when (val result = getCompany(companyId)) {
                is ResultWrapper.Success -> {
                    val c = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            company = c,
                            name = c.name,
                            websiteUrl = c.websiteUrl,
                            mainPhoneNumber = c.mainPhoneNumber,
                            isActive = true,
                        )
                    }
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, fieldErrors = it.fieldErrors.copy(name = null)) }
    }

    fun onWebsiteUrlChange(value: String) {
        _uiState.update { it.copy(websiteUrl = value) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(mainPhoneNumber = value, fieldErrors = it.fieldErrors.copy(phone = null)) }
    }

    fun onIsActiveChange(value: Boolean) {
        _uiState.update { it.copy(isActive = value) }
    }

    fun onSave() {
        if (!validate()) return

        viewModelScope.launch {
            val companyId = _uiState.value.company?.id ?: return@launch
            _uiState.update { it.copy(isSaving = true) }

            val state = _uiState.value
            val request = CompanyRequest(
                id = companyId,
                name = state.name.trim(),
                websiteUrl = state.websiteUrl.trim(),
                mainPhoneNumber = state.mainPhoneNumber.trim(),
                isActive = state.isActive,
            )

            when (val result = updateCompany(companyId, request)) {
                is ResultWrapper.Success -> {
                    val c = result.data
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            company = c,
                            name = c.name,
                            websiteUrl = c.websiteUrl,
                            mainPhoneNumber = c.mainPhoneNumber,
                            saveMessage = "Dados atualizados com sucesso",
                        )
                    }
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(isSaving = false, saveMessage = result.message)
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun dismissSaveMessage() {
        _uiState.update { it.copy(saveMessage = null) }
    }

    fun onImagePicked(uri: Uri) {
        _uiState.update { it.copy(pendingImageUri = uri) }
    }

    fun onCancelImagePick() {
        _uiState.update { it.copy(pendingImageUri = null) }
    }

    fun onSaveImage(imageBytes: ByteArray) {
        viewModelScope.launch {
            val companyId = _uiState.value.company?.id ?: return@launch
            _uiState.update { it.copy(isUploadingImage = true) }

            when (val result = updateCompanyImage(companyId, imageBytes)) {
                is ResultWrapper.Success -> {
                    val c = result.data
                    _uiState.update {
                        it.copy(
                            isUploadingImage = false,
                            pendingImageUri = null,
                            company = c,
                            imageMessage = "Logo atualizada com sucesso",
                        )
                    }
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(isUploadingImage = false, imageMessage = result.message)
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun dismissImageMessage() {
        _uiState.update { it.copy(imageMessage = null) }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        var errors = CompanyFieldErrors()
        var isValid = true

        if (state.name.isBlank()) {
            errors = errors.copy(name = "Nome da empresa é obrigatório")
            isValid = false
        }

        _uiState.update { it.copy(fieldErrors = errors) }
        return isValid
    }
}
