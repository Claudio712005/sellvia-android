package br.com.claus.sellvia.features.product.domain.usecase

import android.net.Uri
import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.product.domain.model.Product
import br.com.claus.sellvia.features.product.domain.repository.IProductRepository

class UpdateProductImageUseCase(private val repository: IProductRepository) {

    suspend operator fun invoke(id: Long, imageUri: Uri): ResultWrapper<Product> {
        return repository.updateImage(id, imageUri)
    }
}
