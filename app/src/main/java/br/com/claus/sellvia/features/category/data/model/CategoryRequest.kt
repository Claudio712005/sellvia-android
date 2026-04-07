package br.com.claus.sellvia.features.category.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequest(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val companyId: Long? = null,
)