package br.com.claus.sellvia.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.company.domain.usecase.GetCompanyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val companyName: String = "",
    val companyLogoUrl: String = "",
)

class HomeViewModel(
    private val tokenManager: TokenManager,
    private val getCompany: GetCompanyUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCompany()
    }

    private fun loadCompany() {
        viewModelScope.launch {
            val companyId = tokenManager.companyId().firstOrNull() ?: return@launch
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getCompany(companyId)) {
                is ResultWrapper.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        companyName = result.data.name,
                        companyLogoUrl = result.data.logoUrl,
                    )
                }
                is ResultWrapper.Error -> _uiState.update { it.copy(isLoading = false) }
                ResultWrapper.Loading -> Unit
            }
        }
    }
}
