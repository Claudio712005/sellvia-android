package br.com.claus.sellvia.features.category.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.category.domain.repository.ICategoryRepository

class DeleteCategoryUseCase(private val repository: ICategoryRepository) {

    suspend operator fun invoke(id: Long): ResultWrapper<Unit> =
        repository.deleteCategory(id)
}