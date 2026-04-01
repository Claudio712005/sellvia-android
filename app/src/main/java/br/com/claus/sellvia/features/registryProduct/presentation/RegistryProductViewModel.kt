package br.com.claus.sellvia.features.registryProduct.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.data.remote.model.request.ProductRequest
import br.com.claus.sellvia.data.remote.model.response.CategoryResponse
import br.com.claus.sellvia.features.registryProduct.data.RegistryProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SubmitResult {
    data object Success : SubmitResult()
    data class Error(val message: String) : SubmitResult()
}

data class RegistryProductFieldErrors(
    val name: String? = null,
    val sku: String? = null,
    val price: String? = null,
    val description: String? = null,
    val stockQuantity: String? = null,
    val productionCost: String? = null,
    val imageUrl: String? = null,
)

data class RegistryProductUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val fieldErrors: RegistryProductFieldErrors = RegistryProductFieldErrors(),
    val localImageUri: Uri? = null,
    val submitResult: SubmitResult? = null,
    val selectedCategory: CategoryResponse? = null,
    val data: ProductRequest = ProductRequest(
        name = "",
        description = "",
        price = 0.0,
        productionCost = 0.0,
        companyId = 0L,
        sku = "",
    )
)

class RegistryProductViewModel(
    private val tokenManager: TokenManager,
    private val repository: RegistryProductRepository
) : ViewModel() {

    fun submit() {
        val state = _uiState.value
        val imageUri = state.localImageUri

        if (imageUri == null) {
            _uiState.update {
                it.copy(submitResult = SubmitResult.Error("Selecione uma imagem para o produto"))
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val token = tokenManager.accessToken.firstOrNull()
                ?: throw Exception("Token de acesso não encontrado")

            if(state.data.companyId == 0L) {
                val companyId = tokenManager.companyId().firstOrNull()
                    ?: throw Exception("ID da empresa não encontrado")
                _uiState.update { it.copy(data = it.data.copy(companyId = companyId)) }
            }

            repository.create(
                request = state.data,
                imageUri = imageUri,
                token = token
            ).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(isLoading = false, submitResult = SubmitResult.Success)
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            submitResult = SubmitResult.Error(e.message ?: "Erro ao salvar produto")
                        )
                    }
                }
            )
        }
    }

    fun dismissSubmitResult() {
        _uiState.update { it.copy(submitResult = null) }
    }

    fun resetForm() {
        _uiState.update { RegistryProductUiState() }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                data = it.data.copy(name = name),
                fieldErrors = it.fieldErrors.copy(name = null)
            )
        }
    }

    fun onSkuChange(sku: String) {
        _uiState.update {
            it.copy(
                data = it.data.copy(sku = sku),
                fieldErrors = it.fieldErrors.copy(sku = null)
            )
        }
    }

    fun onPriceChange(value: String) {
        val parsed = value.replace(",", ".").toDoubleOrNull() ?: 0.0
        _uiState.update {
            it.copy(
                data = it.data.copy(price = parsed),
                fieldErrors = it.fieldErrors.copy(price = null)
            )
        }
    }

    fun onCostChange(value: String) {
        val parsed = value.replace(",", ".").toDoubleOrNull() ?: 0.0
        _uiState.update { it.copy(data = it.data.copy(productionCost = parsed)) }
    }

    fun onStockControlChange(enabled: Boolean) {
        _uiState.update {
            it.copy(
                data = it.data.copy(
                    stockQuantity = if (enabled) (it.data.stockQuantity ?: 0) else null
                )
            )
        }
    }

    fun onStockChange(value: String) {
        val parsed = value.toIntOrNull() ?: 0
        _uiState.update {
            it.copy(
                data = it.data.copy(stockQuantity = parsed)
            )
        }
    }

    private val _uiState = MutableStateFlow(RegistryProductUiState())
    val uiState: StateFlow<RegistryProductUiState> = _uiState.asStateFlow()

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(data = it.data.copy(description = description)) }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(localImageUri = uri) }
    }

    fun onCategorySelected(category: CategoryResponse?) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                data = it.data.copy(categoryId = category?.id)
            )
        }
    }

    fun validateStep(step: Int): Boolean {
        val data = _uiState.value.data
        var errors = RegistryProductFieldErrors()
        var isValid = true

        when (step) {
            0 -> {
                if (data.name.isBlank()) {
                    errors = errors.copy(name = "Nome é obrigatório")
                    isValid = false
                }
                if (data.sku.isBlank()) {
                    errors = errors.copy(sku = "SKU é obrigatório")
                    isValid = false
                }
                if (data.sku.contains(" ")) {
                    errors = errors.copy(sku = "SKU não pode conter espaços")
                    isValid = false
                }
                if (data.description.isBlank()) {
                    errors = errors.copy(description = "Descrição é obrigatória")
                    isValid = false
                }
                if (data.description.length < 10) {
                    errors =
                        errors.copy(description = "Descrição deve ter pelo menos 10 caracteres")
                    isValid = false
                }
            }

            1 -> {
                if (data.price <= 0.0) {
                    errors = errors.copy(price = "Preço deve ser maior que zero")
                    isValid = false
                }
                if (data.productionCost < 0.0) {
                    errors = errors.copy(productionCost = "Custo de produção não pode ser negativo")
                    isValid = false
                }
            }

            2 -> {
                if (data.stockQuantity != null && data.stockQuantity < 0) {
                    errors =
                        errors.copy(stockQuantity = "Quantidade em estoque não pode ser negativa")
                    isValid = false
                }
            }
        }

        _uiState.update { it.copy(fieldErrors = errors) }
        return isValid
    }
}