package br.com.claus.sellvia.features.product.domain.repository

import android.net.Uri
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.product.data.model.ProductRequest
import br.com.claus.sellvia.features.product.data.model.ProductSearchQuery
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination

interface IProductRepository {

    suspend fun findAll(query: ProductSearchQuery): ResultWrapper<Pagination<Product>>

    suspend fun create(request: ProductRequest, imageUri: Uri): ResultWrapper<Unit>

    suspend fun update(request: ProductRequest, id: Long): ResultWrapper<Product>

    suspend fun delete(id: Long): ResultWrapper<Unit>
}