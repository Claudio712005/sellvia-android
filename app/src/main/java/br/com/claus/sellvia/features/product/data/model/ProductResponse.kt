package br.com.claus.sellvia.features.product.data.model

import br.com.claus.sellvia.features.category.data.model.CategoryResponse
import br.com.claus.sellvia.features.product.domain.model.ProductType
import br.com.claus.sellvia.features.product.domain.model.ResourceStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val price: BigDecimal? = null,
    val productionCost: BigDecimal? = null,
    val companyId: Long? = null,
    val status: ResourceStatus? = null,
    val imageUrl: String? = null,
    val category: CategoryResponse? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String? = null,
    val updatedBy: String? = null,
    val sku: String? = null,
    val stockQuantity: Int? = null,
    val type: ProductType? = null,
    val externalLink: String? = null,
    val whatsappMessage: String? = null,
)