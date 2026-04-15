package br.com.claus.sellvia.features.product.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.product.data.model.ProductRequest
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.features.product.domain.repository.IProductRepository

class UpdateProductUseCase(private val repository: IProductRepository) {

    suspend operator fun invoke(id: Long, request: ProductRequest): ResultWrapper<Product> {
        if (request.name.isBlank())
            return ResultWrapper.Error(message = "Nome é obrigatório")
        if (request.sku.isBlank())
            return ResultWrapper.Error(message = "SKU é obrigatório")
        if (request.sku.contains(" "))
            return ResultWrapper.Error(message = "SKU não pode conter espaços")
        if (request.description.isBlank())
            return ResultWrapper.Error(message = "Descrição é obrigatória")
        if (request.description.length < 10)
            return ResultWrapper.Error(message = "Descrição deve ter pelo menos 10 caracteres")
        if (request.price <= 0.0)
            return ResultWrapper.Error(message = "Preço deve ser maior que zero")
        if (request.productionCost < 0.0)
            return ResultWrapper.Error(message = "Custo de produção não pode ser negativo")
        if (request.stockQuantity != null && request.stockQuantity < 0)
            return ResultWrapper.Error(message = "Quantidade em estoque não pode ser negativa")

        return repository.update(request, id)
    }
}
