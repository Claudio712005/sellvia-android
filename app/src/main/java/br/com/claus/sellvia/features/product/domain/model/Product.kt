package br.com.claus.sellvia.features.product.domain.model

import br.com.claus.sellvia.features.product.domain.model.ProductType
import br.com.claus.sellvia.features.product.domain.model.ResourceStatus
import br.com.claus.sellvia.features.category.domain.model.Category
import java.math.BigDecimal
import java.time.LocalDateTime

data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val productionCost: BigDecimal,
    val companyId: Long,
    val status: ResourceStatus,
    val imageUrl: String?,
    val category: Category?,
    val sku: String,
    val stockQuantity: Int?,
    val type: ProductType,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String? = null,
    val updatedBy: String? = null,
    val externalLink: String? = null,
    val whatsappMessage: String? = null,
)