package br.com.claus.sellvia.features.catalog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.catalog.domain.model.CatalogDisplayOptions
import br.com.claus.sellvia.features.catalog.domain.model.CatalogFilterOptions
import br.com.claus.sellvia.features.catalog.domain.usecase.DownloadCatalogUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CatalogUiState(
    val showSheet: Boolean = false,
    val displayOptions: CatalogDisplayOptions = CatalogDisplayOptions(),
    val filterOptions: CatalogFilterOptions = CatalogFilterOptions(),
    val isEnqueuing: Boolean = false,
    val downloadMessage: String? = null,
)

class CatalogViewModel(
    private val tokenManager: TokenManager,
    private val downloadCatalog: DownloadCatalogUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    fun onOpenSheet() {
        _uiState.update { it.copy(showSheet = true) }
    }

    fun onDismissSheet() {
        _uiState.update { it.copy(showSheet = false) }
    }

    fun onDisplayOptionsUpdate(update: CatalogDisplayOptions.() -> CatalogDisplayOptions) {
        _uiState.update { it.copy(displayOptions = it.displayOptions.update()) }
    }

    fun onFilterOptionsUpdate(update: CatalogFilterOptions.() -> CatalogFilterOptions) {
        _uiState.update { it.copy(filterOptions = it.filterOptions.update()) }
    }

    fun onDownload() {
        viewModelScope.launch {
            val companyId = tokenManager.companyId().firstOrNull()
            val token = tokenManager.accessToken.firstOrNull()

            if (companyId == null) {
                _uiState.update { it.copy(downloadMessage = "Empresa não identificada") }
                return@launch
            }
            if (token == null) {
                _uiState.update { it.copy(downloadMessage = "Sessão expirada. Faça login novamente.") }
                return@launch
            }

            _uiState.update { it.copy(isEnqueuing = true) }

            when (downloadCatalog(
                companyId = companyId,
                token = token,
                filter = _uiState.value.filterOptions,
                display = _uiState.value.displayOptions,
            )) {
                is ResultWrapper.Success -> _uiState.update {
                    it.copy(
                        isEnqueuing = false,
                        showSheet = false,
                        downloadMessage = "Download iniciado! Acompanhe na barra de notificações.",
                    )
                }
                is ResultWrapper.Error -> _uiState.update {
                    it.copy(
                        isEnqueuing = false,
                        downloadMessage = "Erro ao iniciar download. Tente novamente.",
                    )
                }
                ResultWrapper.Loading -> Unit
            }
        }
    }

    fun dismissDownloadMessage() {
        _uiState.update { it.copy(downloadMessage = null) }
    }
}
