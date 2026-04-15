package br.com.claus.sellvia.features.product.data.model

import br.com.claus.sellvia.features.product.domain.model.ProductType
import br.com.claus.sellvia.features.product.domain.model.ResourceStatus

data class ProductRequest(
    var id: String? =null,
    val name: String,
    val description: String,
    val price: Double,
    val productionCost: Double,
    val companyId: Long,
    val status: ResourceStatus = ResourceStatus.ACTIVE,
    var imageUrl: String? = null,
    val categoryId: Long? = null,
    val sku: String,
    val stockQuantity: Int? = null,
    val type: ProductType = ProductType.PHYSICAL,
    val externalLink: String? = null,
    val whatsappMessage: String? = null,
)
