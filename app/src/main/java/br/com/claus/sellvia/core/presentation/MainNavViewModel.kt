package br.com.claus.sellvia.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.core.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainNavViewModel(
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _fabVisible = MutableStateFlow(false)
    val fabVisible: StateFlow<Boolean> = _fabVisible.asStateFlow()

    private val _fabAction = MutableStateFlow<(() -> Unit)?>(null)
    val fabAction: StateFlow<(() -> Unit)?> = _fabAction.asStateFlow()

    private val _openCategoryModal = MutableStateFlow(false)
    val openCategoryModal: StateFlow<Boolean> = _openCategoryModal.asStateFlow()

    private val _userRole = MutableStateFlow<UserRole?>(null)
    val userRole: StateFlow<UserRole?> = _userRole.asStateFlow()

    init {
        viewModelScope.launch {
            tokenManager.userRole().collect { _userRole.value = it }
        }
    }

    fun showFab(action: () -> Unit) {
        _fabVisible.value = true
        _fabAction.value = action
    }

    fun hideFab() {
        _fabVisible.value = false
        _fabAction.value = null
    }

    fun openModal() {
        _openCategoryModal.value = true
    }

    fun closeModal() {
        _openCategoryModal.value = false
    }
}
