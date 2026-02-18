package br.com.claus.sellvia.features.listCategory.presentation

import br.com.claus.sellvia.core.ui.BaseState

data class CategoryFormState(
    val name: String = "",
    val description: String = "",
    override val isLoading: Boolean = false,
    override val error: String? = null,
    override val isSuccess: Boolean = false
) : BaseState