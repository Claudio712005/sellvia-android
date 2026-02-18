package br.com.claus.sellvia.features.listCategory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.ui.UIEvent
import br.com.claus.sellvia.data.remote.model.ApiErrorResponse
import br.com.claus.sellvia.data.remote.model.parseErrorBody
import br.com.claus.sellvia.data.remote.model.request.CategoryRequest
import br.com.claus.sellvia.data.remote.model.response.CategoryResponse
import br.com.claus.sellvia.features.listCategory.data.ListCategoryRepository
import br.com.claus.sellvia.features.listCategory.presentation.CategoryFormState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

class ActionsCategoryViewModel(
    private val tokenManager: TokenManager,
    private val repository: ListCategoryRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<CategoryResponse?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _uiState = MutableStateFlow(CategoryFormState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UIEvent>()
    val events = _events.asSharedFlow()

    fun onSaveCategory() {
        viewModelScope.launch {
            try {
                val id = _selectedCategory.value?.id

                _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

                val token = tokenManager.accessToken.firstOrNull()
                    ?: throw Exception("Token não encontrado")

                val request = CategoryRequest(
                    name = _uiState.value.name,
                    description = _uiState.value.description,
                    companyId = tokenManager.companyId().firstOrNull()
                )

                var response: Response<*>

                if (id != null) {
                    response = repository.update(
                        id = id ?: throw Exception("ID da categoria não encontrado"),
                        request = request,
                        token = token
                    )
                } else {
                    response = repository.registry(request, token)
                }

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            name = "",
                            description = "",
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                    _events.emit(UIEvent.SaveSuccess)
                } else {
                    val errorMsg = ApiErrorResponse.Companion.parseErrorBody(response.errorBody())
                    _uiState.update {
                        it.copy(
                            error = errorMsg,
                            isLoading = false,
                            isSuccess = false
                        )
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update { it.copy(error = e.message, isLoading = false, isSuccess = false) }
            }
        }
    }

    fun onDeleteCategory() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

                val token = tokenManager.accessToken.firstOrNull()
                    ?: throw Exception("Token não encontrado")

                val categoryId =
                    _selectedCategory.value?.id ?: throw Exception("Categoria não selecionada")

                val response = repository.delete(categoryId, token)

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            name = "",
                            description = "",
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                    _selectedCategory.update { null }

                    _events.emit(UIEvent.DeleteSuccess)
                } else {
                    val errorMsg = ApiErrorResponse.Companion.parseErrorBody(response.errorBody())
                    _uiState.update {
                        it.copy(
                            error = errorMsg,
                            isLoading = false,
                            isSuccess = false
                        )
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update { it.copy(error = e.message, isLoading = false, isSuccess = false) }
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName, isSuccess = false) }
    }

    fun onDescriptionChange(newDesc: String) {
        _uiState.update { it.copy(description = newDesc, isSuccess = false) }
    }

    fun onSelectCategory(category: CategoryResponse?) {
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