package br.com.claus.sellvia.features.product.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.product.domain.repository.IProductRepository

class DeleteProductUseCase(private val repository: IProductRepository) {
    suspend operator fun invoke(id: Long): ResultWrapper<Unit> = repository.delete(id)
}
