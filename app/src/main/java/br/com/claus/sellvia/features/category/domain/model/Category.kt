package br.com.claus.sellvia.features.category.domain.model

import java.time.LocalDateTime

data class Category(
    val id: Long,
    val name: String,
    val description: String,
    val companyId: Long,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String? = null,
    val updatedBy: String? = null
)