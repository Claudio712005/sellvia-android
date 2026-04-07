package br.com.claus.sellvia.features.category.data.model

import java.time.LocalDateTime

data class CategoryResponse(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val companyId: Long? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String? = null,
    val updatedBy: String? = null,
)