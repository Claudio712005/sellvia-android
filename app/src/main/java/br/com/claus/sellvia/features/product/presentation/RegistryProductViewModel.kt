package br.com.claus.sellvia.features.product.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.presentation.LoadingViewModel
import br.com.claus.sellvia.features.product.data.model.ProductRequest
import br.com.claus.sellvia.features.category.domain.model.Category
import br.com.claus.sellvia.features.product.domain.usecase.CreateProductUseCase
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

object UploadConfig {
    const val MAX_FILE_SIZE_MB = 10
    const val MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024
    const val FILE_SIZE_THRESHOLD_MB = 2
    const val FILE_SIZE_THRESHOLD_BYTES = FILE_SIZE_THRESHOLD_MB * 1024 * 1024
}

data class RegistryProductFieldErrors(
    val name: String? = null,
    val sku: String? = null,
    val price: String? = null,
    val description: String? = null,
    val stockQuantity: String? = null,
    val productionCost: String? = null,
    val imageUrl: String? = null,
    val externalLink: String? = null,
)

data class RegistryProductUiState(
    val error: String? = null,
    val fieldErrors: RegistryProductFieldErrors = RegistryProductFieldErrors(),
    val localImageUri: Uri? = null,
    val submitResult: SubmitResult? = null,
    val selectedCategory: Category? = null,
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
    private val context: Context,
    private val tokenManager: TokenManager,
    private val createProduct: CreateProductUseCase,
    private val loadingViewModel: LoadingViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistryProductUiState())
    val uiState: StateFlow<RegistryProductUiState> = _uiState.asStateFlow()

    private fun getRealFileSize(uri: Uri): Long {
        context.contentResolver.openInputStream(uri)?.use { input ->
            var total = 0L
            val buffer = ByteArray(8 * 1024)

            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                total += read
            }
            return total
        }
        return 0
    }

    private fun validateImageSize(uri: Uri): Pair<Boolean, String?> {
        return try {
            val contentResolver = context.contentResolver

            val sizeFromCursor = contentResolver.query(
                uri,
                arrayOf(android.provider.OpenableColumns.SIZE),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
                    if (index != -1) cursor.getLong(index).takeIf { it > 0 } else null
                } else null
            }

            val fileSize = getRealFileSize(uri)
            Log.d("UPLOAD_DEBUG", "File size bytes: $fileSize")
            Log.d("UPLOAD_DEBUG", "File size MB: ${fileSize / (1024.0 * 1024.0)}")

            when {
                fileSize <= 0 -> Pair(false, "Arquivo inválido")

                fileSize > UploadConfig.MAX_FILE_SIZE_BYTES -> Pair(
                    false,
                    "Imagem muito grande. Máximo: ${UploadConfig.MAX_FILE_SIZE_MB}MB " +
                            "(atual: ${"%.1f".format(fileSize / (1024.0 * 1024.0))}MB)"
                )

                else -> Pair(true, null)
            }

        } catch (e: Exception) {
            Pair(false, "Erro ao validar imagem: ${e.message}")
        }
    }

    fun submit() {
        val state = _uiState.value
        val imageUri = state.localImageUri

        if (imageUri == null) {
            _uiState.update { it.copy(submitResult = SubmitResult.Error("Selecione uma imagem para o produto")) }
            return
        }

        val (isValidSize, sizeError) = validateImageSize(imageUri)
        if (!isValidSize) {
            _uiState.update { it.copy(submitResult = SubmitResult.Error(sizeError ?: "Imagem inválida")) }
            return
        }

        viewModelScope.launch {
            loadingViewModel.show()

            val companyId = if (state.data.companyId != 0L) {
                state.data.companyId
            } else {
                tokenManager.companyId().firstOrNull() ?: run {
                    loadingViewModel.hide()
                    _uiState.update { it.copy(submitResult = SubmitResult.Error("ID da empresa não encontrado")) }
                    return@launch
                }
            }

            val requestData = state.data.copy(companyId = companyId)
            _uiState.update { it.copy(data = requestData) }

            when (val result = createProduct(requestData, imageUri)) {
                is ResultWrapper.Success -> {
                    loadingViewModel.hide()
                    _uiState.update { it.copy(submitResult = SubmitResult.Success) }
                }
                is ResultWrapper.Error -> {
                    loadingViewModel.hide()
                    _uiState.update { it.copy(submitResult = SubmitResult.Error(result.message)) }
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun isLoading(): StateFlow<Boolean> = loadingViewModel.isLoading

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
                data = it.data.copy(sku = sku.uppercase()),
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
        _uiState.update { it.copy(data = it.data.copy(stockQuantity = parsed)) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(data = it.data.copy(description = description)) }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(localImageUri = uri) }
    }

    fun onCategorySelected(category: Category?) {
        _uiState.update {
            it.copy(selectedCategory = category, data = it.data.copy(categoryId = category?.id))
        }
    }

    fun onExternalLinkChange(value: String) {
        _uiState.update {
            it.copy(
                data = it.data.copy(externalLink = value.ifBlank { null }),
                fieldErrors = it.fieldErrors.copy(externalLink = null)
            )
        }
    }

    fun onWhatsappMessageChange(value: String) {
        _uiState.update {
            it.copy(data = it.data.copy(whatsappMessage = value.ifBlank { null }))
        }
    }

    fun validateStep(step: Int): Boolean {
        val data = _uiState.value.data
        var errors = RegistryProductFieldErrors()
        var isValid = true

        when (step) {
            0 -> {
                if (data.name.isBlank()) {
                    errors = errors.copy(name = "Nome é obrigatório"); isValid = false
                }
                if (data.sku.isBlank()) {
                    errors = errors.copy(sku = "SKU é obrigatório"); isValid = false
                }
                if (data.sku.contains(" ")) {
                    errors = errors.copy(sku = "SKU não pode conter espaços"); isValid = false
                }
                if (data.description.isBlank()) {
                    errors = errors.copy(description = "Descrição é obrigatória"); isValid = false
                }
                if (data.description.length < 10) {
                    errors =
                        errors.copy(description = "Descrição deve ter pelo menos 10 caracteres"); isValid =
                        false
                }
                if (!data.externalLink.isNullOrBlank()) {
                    if (data.externalLink.length > 500) {
                        errors = errors.copy(externalLink = "Link externo deve ter no máximo 500 caracteres")
                        isValid = false
                    } else if (!Patterns.WEB_URL.matcher(data.externalLink).matches()) {
                        errors = errors.copy(externalLink = "Link externo inválido. Informe uma URL válida")
                        isValid = false
                    }
                }
            }

            1 -> {
                if (data.price <= 0.0) {
                    errors = errors.copy(price = "Preço deve ser maior que zero"); isValid = false
                }
                if (data.productionCost < 0.0) {
                    errors =
                        errors.copy(productionCost = "Custo de produção não pode ser negativo"); isValid =
                        false
                }
            }

            2 -> {
                if (data.stockQuantity != null && data.stockQuantity < 0) {
                    errors =
                        errors.copy(stockQuantity = "Quantidade em estoque não pode ser negativa"); isValid =
                        false
                }
            }
        }

        _uiState.update { it.copy(fieldErrors = errors) }
        return isValid
    }
}