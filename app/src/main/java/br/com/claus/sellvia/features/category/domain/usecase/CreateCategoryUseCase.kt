package br.com.claus.sellvia.features.category.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.category.domain.repository.ICategoryRepository

class CreateCategoryUseCase(private val repository: ICategoryRepository) {

    suspend operator fun invoke(
        name: String,
        description: String,
        companyId: Long
    ): ResultWrapper<Unit> {
        if (name.isBlank())
            return ResultWrapper.Error(message = "O campo 'Nome' é obrigatório")
        if (description.isBlank())
            return ResultWrapper.Error(message = "O campo 'Descrição' é obrigatório")

        return repository.createCategory(name.trim(), description.trim(), companyId)
    }
}