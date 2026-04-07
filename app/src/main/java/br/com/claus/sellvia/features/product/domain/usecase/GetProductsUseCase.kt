package br.com.claus.sellvia.features.product.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.product.data.model.ProductSearchQuery
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.features.product.domain.repository.IProductRepository
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination

class GetProductsUseCase(private val repository: IProductRepository) {

    suspend operator fun invoke(query: ProductSearchQuery): ResultWrapper<Pagination<Product>> =
        repository.findAll(query)
}