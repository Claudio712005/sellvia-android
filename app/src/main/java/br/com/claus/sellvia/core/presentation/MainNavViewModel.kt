package br.com.claus.sellvia.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainNavViewModel : ViewModel() {

    private val _fabVisible = MutableStateFlow(false)
    val fabVisible: StateFlow<Boolean> = _fabVisible.asStateFlow()

    private val _fabAction = MutableStateFlow<(() -> Unit)?>(null)
    val fabAction: StateFlow<(() -> Unit)?> = _fabAction.asStateFlow()

    private val _openCategoryModal = MutableStateFlow(false)
    val openCategoryModal: StateFlow<Boolean> = _openCategoryModal.asStateFlow()

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