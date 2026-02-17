package br.com.claus.sellvia.data.remote.model.request

data class CategoryRequest(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val companyId: Long? = null,
)