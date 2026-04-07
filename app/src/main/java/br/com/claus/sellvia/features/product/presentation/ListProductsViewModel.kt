package br.com.claus.sellvia.features.product.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.model.Direction
import br.com.claus.sellvia.features.product.data.model.ProductSearchQuery
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.features.product.domain.usecase.GetProductsUseCase
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination
import br.com.claus.sellvia.ui.components.paginationTemplate.models.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ListProductUiState(
    val isLoading: Boolean = false,
    val paginationData: Pagination<Product>? = null,
    val error: String? = null
)

class ListProductsViewModel(
    private val tokenManager: TokenManager,
    private val getProducts: GetProductsUseCase
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
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
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
                is ResultWrapper.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    paginationData = result.data
                )
                is ResultWrapper.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.message
                )
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun updateSort(newSort: SortOption) {
        _selectedSort.value = newSort
        fetchCategories(page = 0)
    }
}