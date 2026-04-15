package br.com.claus.sellvia.ui.components.paginationTemplate.models

data class Pagination<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 0,
    val perPage: Int = 20,
    val totalPages: Int = 1,
    val totalItems: Long = 0L
)
