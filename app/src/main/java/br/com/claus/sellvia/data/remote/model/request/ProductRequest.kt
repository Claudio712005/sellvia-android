package br.com.claus.sellvia.data.remote.model.request

import br.com.claus.sellvia.data.enums.ProductType
import br.com.claus.sellvia.data.enums.ResourceStatus

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
)
