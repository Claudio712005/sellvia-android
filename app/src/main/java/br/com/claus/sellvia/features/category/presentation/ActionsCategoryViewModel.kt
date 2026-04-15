package br.com.claus.sellvia.features.category.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.ui.UIEvent
import br.com.claus.sellvia.features.category.domain.model.Category
import br.com.claus.sellvia.features.category.domain.usecase.CreateCategoryUseCase
import br.com.claus.sellvia.features.category.domain.usecase.DeleteCategoryUseCase
import br.com.claus.sellvia.features.category.domain.usecase.UpdateCategoryUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActionsCategoryViewModel(
    private val tokenManager: TokenManager,
    private val createCategory: CreateCategoryUseCase,
    private val updateCategory: UpdateCategoryUseCase,
    private val deleteCategory: DeleteCategoryUseCase
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _uiState = MutableStateFlow(CategoryFormState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UIEvent>()
    val events = _events.asSharedFlow()

    fun onSaveCategory() {
        viewModelScope.launch {
            val id = _selectedCategory.value?.id
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

            val companyId = tokenManager.companyId().firstOrNull() ?: run {
                _uiState.update { it.copy(error = "Empresa não identificada", isLoading = false) }
                return@launch
            }

            val result = if (id != null) {
                updateCategory(id, _uiState.value.name, _uiState.value.description, companyId)
            } else {
                createCategory(_uiState.value.name, _uiState.value.description, companyId)
            }

            when (result) {
                is ResultWrapper.Success -> {
                    _uiState.update { it.copy(name = "", description = "", isLoading = false, isSuccess = true) }
                    _events.emit(UIEvent.SaveSuccess)
                }
                is ResultWrapper.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoading = false, isSuccess = false) }
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun onDeleteCategory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

            val categoryId = _selectedCategory.value?.id ?: run {
                _uiState.update { it.copy(error = "Categoria não selecionada", isLoading = false) }
                return@launch
            }

            when (val result = deleteCategory(categoryId)) {
                is ResultWrapper.Success -> {
                    _uiState.update { it.copy(name = "", description = "", isLoading = false, isSuccess = true) }
                    _selectedCategory.update { null }
                    _events.emit(UIEvent.DeleteSuccess)
                }
                is ResultWrapper.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoading = false, isSuccess = false) }
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName, isSuccess = false) }
    }

    fun onDescriptionChange(newDesc: String) {
        _uiState.update { it.copy(description = newDesc, isSuccess = false) }
    }

    fun onSelectCategory(category: Category?) {
        _selectedCategory.value = category
        _uiState.update {
            it.copy(
                name = category?.name ?: "",
                description = category?.description ?: "",
                isSuccess = false,
                error = null
            )
        }
    }
}