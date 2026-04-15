package br.com.claus.sellvia.features.product.presentation

import android.content.Context
import android.net.Uri
import android.util.Patterns
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.model.Direction
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.utils.formatDouble
import br.com.claus.sellvia.features.product.data.model.ProductRequest
import br.com.claus.sellvia.features.product.data.model.ProductSearchQuery
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.features.product.domain.usecase.DeleteProductUseCase
import br.com.claus.sellvia.features.product.domain.usecase.GetProductsUseCase
import br.com.claus.sellvia.features.product.domain.usecase.UpdateProductUseCase
import br.com.claus.sellvia.features.product.domain.usecase.UpdateProductImageUseCase
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import br.com.claus.sellvia.ui.components.paginationTemplate.models.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ListProductUiState(
    val isLoading: Boolean = false,
    val paginationData: Pagination<Product>? = null,
    val error: String? = null,
    val selectedProduct: Product? = null,
    val isEditMode: Boolean = false,
    val editFormData: ProductRequest? = null,
    val editFieldErrors: RegistryProductFieldErrors = RegistryProductFieldErrors(),
    val showDeleteConfirm: Boolean = false,
    val showEditSaveConfirm: Boolean = false,
    val actionResult: SubmitResult? = null,
    val editImageUri: Uri? = null,
    val showImageUpdateConfirm: Boolean = false,
    val isUploadingImage: Boolean = false,
)

class ListProductsViewModel(
    private val context: Context,
    private val tokenManager: TokenManager,
    private val getProducts: GetProductsUseCase,
    private val deleteProduct: DeleteProductUseCase,
    private val updateProduct: UpdateProductUseCase,
    private val updateProductImage: UpdateProductImageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListProductUiState())
    val uiState: StateFlow<ListProductUiState> = _uiState.asStateFlow()

    val sortOptions = listOf(
        SortOption("Nome (A-Z)", "name_ASC", Icons.Default.SortByAlpha),
        SortOption("Data (Mais novos)", "createdAt_DESC", Icons.Default.CalendarToday)
    )

    private val _selectedSort = MutableStateFlow(sortOptions[0])
    val selectedSort = _selectedSort.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    init {
        fetchCategories()
    }

    fun fetchCategories(page: Int = _currentPage.value) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            _currentPage.value = page

            val companyId = tokenManager.companyId().firstOrNull()
            val sortParams = _selectedSort.value.value.split("_")
            val sortField = sortParams[0]
            val sortDirection = Direction.valueOf(sortParams[1])

            when (val result = getProducts(
                ProductSearchQuery(
                    page = page,
                    perPage = 10,
                    sort = sortField,
                    direction = sortDirection,
                    companyId = companyId
                )
            )) {
                is ResultWrapper.Success -> _uiState.update {
                    it.copy(isLoading = false, paginationData = result.data)
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun updateSort(newSort: SortOption) {
        _selectedSort.value = newSort
        fetchCategories(page = 0)
    }

    fun onSelectProduct(product: Product) {
        _uiState.update {
            it.copy(
                selectedProduct = product,
                isEditMode = false,
                editFormData = product.toEditRequest(),
                editFieldErrors = RegistryProductFieldErrors(),
            )
        }
    }

    fun onDismissSheet() {
        _uiState.update {
            it.copy(
                selectedProduct = null,
                isEditMode = false,
                editFormData = null,
                editFieldErrors = RegistryProductFieldErrors(),
                showDeleteConfirm = false,
                showEditSaveConfirm = false,
                editImageUri = null,
                showImageUpdateConfirm = false,
            )
        }
    }

    fun onEditRequest() {
        _uiState.update { it.copy(isEditMode = true) }
    }

    fun onDeleteRequest() {
        _uiState.update { it.copy(showDeleteConfirm = true) }
    }

    fun onDismissDeleteConfirm() {
        _uiState.update { it.copy(showDeleteConfirm = false) }
    }

    fun onConfirmDelete() {
        val product = _uiState.value.selectedProduct ?: return
        viewModelScope.launch {
            when (val result = deleteProduct(product.id)) {
                is ResultWrapper.Success -> {
                    val updatedItems = _uiState.value.paginationData?.items
                        ?.filter { it.id != product.id } ?: emptyList()
                    _uiState.update {
                        it.copy(
                            paginationData = it.paginationData?.copy(items = updatedItems),
                            selectedProduct = null,
                            isEditMode = false,
                            editFormData = null,
                            showDeleteConfirm = false,
                            actionResult = SubmitResult.Success,
                        )
                    }
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(
                        showDeleteConfirm = false,
                        actionResult = SubmitResult.Error(result.message),
                    )
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun onEditSaveRequest() {
        if (!validateEditFields()) return
        _uiState.update { it.copy(showEditSaveConfirm = true) }
    }

    fun onDismissEditSaveConfirm() {
        _uiState.update { it.copy(showEditSaveConfirm = false) }
    }

    fun onConfirmEditSave() {
        val product = _uiState.value.selectedProduct ?: return
        val formData = _uiState.value.editFormData ?: return
        viewModelScope.launch {
            when (val result = updateProduct(product.id, formData)) {
                is ResultWrapper.Success -> {
                    val updatedItems = _uiState.value.paginationData?.items
                        ?.map { if (it.id == product.id) result.data else it } ?: emptyList()
                    _uiState.update {
                        it.copy(
                            paginationData = it.paginationData?.copy(items = updatedItems),
                            selectedProduct = null,
                            isEditMode = false,
                            editFormData = null,
                            showEditSaveConfirm = false,
                            actionResult = SubmitResult.Success,
                        )
                    }
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(
                        showEditSaveConfirm = false,
                        actionResult = SubmitResult.Error(result.message),
                    )
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun dismissActionResult() {
        _uiState.update { it.copy(actionResult = null) }
    }

    fun onEditNameChange(name: String) {
        _uiState.update {
            it.copy(
                editFormData = it.editFormData?.copy(name = name),
                editFieldErrors = it.editFieldErrors.copy(name = null),
            )
        }
    }

    fun onEditSkuChange(sku: String) {
        _uiState.update {
            it.copy(
                editFormData = it.editFormData?.copy(sku = sku.uppercase()),
                editFieldErrors = it.editFieldErrors.copy(sku = null),
            )
        }
    }

    fun onEditDescriptionChange(description: String) {
        _uiState.update {
            it.copy(
                editFormData = it.editFormData?.copy(description = description),
                editFieldErrors = it.editFieldErrors.copy(description = null),
            )
        }
    }

    fun onEditPriceChange(value: String) {
        val parsed = value.replace(",", ".").toDoubleOrNull() ?: 0.0
        _uiState.update {
            it.copy(
                editFormData = it.editFormData?.copy(price = parsed),
                editFieldErrors = it.editFieldErrors.copy(price = null),
            )
        }
    }

    fun onEditCostChange(value: String) {
        val parsed = value.replace(",", ".").toDoubleOrNull() ?: 0.0
        _uiState.update {
            it.copy(editFormData = it.editFormData?.copy(productionCost = parsed))
        }
    }

    fun onEditStockControlChange(enabled: Boolean) {
        _uiState.update {
            it.copy(
                editFormData = it.editFormData?.copy(
                    stockQuantity = if (enabled) (it.editFormData.stockQuantity ?: 0) else null
                )
            )
        }
    }

    fun onEditStockChange(value: String) {
        val parsed = value.toIntOrNull() ?: 0
        _uiState.update {
            it.copy(editFormData = it.editFormData?.copy(stockQuantity = parsed))
        }
    }

    fun onEditExternalLinkChange(value: String) {
        _uiState.update {
            it.copy(
                editFormData = it.editFormData?.copy(externalLink = value.ifBlank { null }),
                editFieldErrors = it.editFieldErrors.copy(externalLink = null),
            )
        }
    }

    fun onEditWhatsappMessageChange(value: String) {
        _uiState.update {
            it.copy(editFormData = it.editFormData?.copy(whatsappMessage = value.ifBlank { null }))
        }
    }

    fun onEditImageSelected(uri: Uri?) {
        _uiState.update { it.copy(editImageUri = uri) }
    }

    fun onImageUpdateRequest() {
        val uri = _uiState.value.editImageUri ?: return
        val (valid, error) = validateImageSize(uri)
        if (!valid) {
            _uiState.update { it.copy(actionResult = SubmitResult.Error(error ?: "Imagem inválida")) }
            return
        }
        _uiState.update { it.copy(showImageUpdateConfirm = true) }
    }

    fun onDismissImageUpdateConfirm() {
        _uiState.update { it.copy(showImageUpdateConfirm = false) }
    }

    fun onConfirmImageUpdate() {
        val product = _uiState.value.selectedProduct ?: return
        val uri = _uiState.value.editImageUri ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isUploadingImage = true, showImageUpdateConfirm = false) }
            when (val result = updateProductImage(product.id, uri)) {
                is ResultWrapper.Success -> {
                    val updatedItems = _uiState.value.paginationData?.items
                        ?.map { if (it.id == product.id) result.data else it } ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isUploadingImage = false,
                            editImageUri = null,
                            selectedProduct = result.data,
                            paginationData = it.paginationData?.copy(items = updatedItems),
                            actionResult = SubmitResult.Success,
                        )
                    }
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(
                        isUploadingImage = false,
                        actionResult = SubmitResult.Error(result.message),
                    )
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    private fun validateImageSize(uri: Uri): Pair<Boolean, String?> {
        return try {
            var fileSize = 0L
            context.contentResolver.openInputStream(uri)?.use { input ->
                val buffer = ByteArray(8 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    fileSize += read
                }
            }
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

    private fun validateEditFields(): Boolean {
        val data = _uiState.value.editFormData ?: return false
        var errors = RegistryProductFieldErrors()
        var isValid = true

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
            errors = errors.copy(description = "Descrição deve ter pelo menos 10 caracteres"); isValid = false
        }
        if (data.price <= 0.0) {
            errors = errors.copy(price = "Preço deve ser maior que zero"); isValid = false
        }
        if (data.productionCost < 0.0) {
            errors = errors.copy(productionCost = "Custo de produção não pode ser negativo"); isValid = false
        }
        if (data.stockQuantity != null && data.stockQuantity < 0) {
            errors = errors.copy(stockQuantity = "Quantidade em estoque não pode ser negativa"); isValid = false
        }
        if (!data.externalLink.isNullOrBlank()) {
            if (data.externalLink.length > 500) {
                errors = errors.copy(externalLink = "Link externo deve ter no máximo 500 caracteres"); isValid = false
            } else if (!Patterns.WEB_URL.matcher(data.externalLink).matches()) {
                errors = errors.copy(externalLink = "Link externo inválido. Informe uma URL válida"); isValid = false
            }
        }

        _uiState.update { it.copy(editFieldErrors = errors) }
        return isValid
    }
}

private fun Product.toEditRequest() = ProductRequest(
    id = id.toString(),
    name = name,
    description = description,
    price = price.toDouble(),
    productionCost = productionCost.toDouble(),
    companyId = companyId,
    status = status,
    imageUrl = imageUrl,
    categoryId = category?.id,
    sku = sku,
    stockQuantity = stockQuantity,
    type = type,
    externalLink = externalLink,
    whatsappMessage = whatsappMessage,
)
