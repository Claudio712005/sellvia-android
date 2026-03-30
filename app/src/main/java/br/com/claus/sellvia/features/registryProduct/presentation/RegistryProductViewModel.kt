package br.com.claus.sellvia.features.registryProduct.presentation

import androidx.lifecycle.ViewModel
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.data.remote.model.request.ProductRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegistryProductUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
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
    private val tokenManager: TokenManager
) : ViewModel() {

    fun onPriceChange(value: String) {
        val parsed = value.replace(",", ".").toDoubleOrNull() ?: 0.0
        _uiState.update { it.copy(data = it.data.copy(price = parsed)) }
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

    // ─── Step 1 ───────────────────────────────────────────────────────────────

    fun onNameChange(name: String) {
        _uiState.update { it.copy(data = it.data.copy(name = name)) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(data = it.data.copy(description = description)) }
    }

    fun onSkuChange(sku: String) {
        _uiState.update { it.copy(data = it.data.copy(sku = sku)) }
    }

    fun onImageClick() {

    }
}