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
    val downloadStatus: DownloadStatus = DownloadStatus.Idle,
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
        if (_uiState.value.downloadStatus is DownloadStatus.InProgress) return
        _uiState.update { it.copy(downloadStatus = DownloadStatus.InProgress, showSheet = false) }

        viewModelScope.launch {
            val companyId = tokenManager.companyId().firstOrNull()
            if (companyId == null) {
                _uiState.update {
                    it.copy(downloadStatus = DownloadStatus.Error("Empresa não identificada."))
                }
                return@launch
            }

            _uiState.update {
                it.copy(downloadStatus = when (val result = downloadCatalog(
                    companyId = companyId,
                    filter = it.filterOptions,
                    display = it.displayOptions,
                )) {
                    is ResultWrapper.Success -> DownloadStatus.Success(result.data)
                    is ResultWrapper.Error -> DownloadStatus.Error(
                        "Erro ao gerar o catálogo. Verifique sua conexão e tente novamente."
                    )
                    ResultWrapper.Loading -> DownloadStatus.InProgress
                })
            }
        }
    }

    fun onDismissDownload() {
        _uiState.update { it.copy(downloadStatus = DownloadStatus.Idle) }
    }
}
