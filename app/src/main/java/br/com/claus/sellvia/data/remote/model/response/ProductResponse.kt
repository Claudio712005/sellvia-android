package br.com.claus.sellvia.data.remote.model.response

import br.com.claus.sellvia.data.enums.ProductType
import br.com.claus.sellvia.data.enums.ResourceStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val productionCost: BigDecimal,
    val companyId: Long,
    val status: ResourceStatus,
    val imageUrl: String?,
    val category: CategoryResponse?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val createdBy: String?,
    val updatedBy: String?,
    val sku: String,
    val stockQuantity: Int?,
    val type: ProductType
)