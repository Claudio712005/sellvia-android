package br.com.claus.sellvia.features.category.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.category.domain.model.Category
import br.com.claus.sellvia.features.category.domain.repository.ICategoryRepository

class UpdateCategoryUseCase(private val repository: ICategoryRepository) {

    suspend operator fun invoke(
        id: Long,
        name: String,
        description: String,
        companyId: Long
    ): ResultWrapper<Category> {
        if (name.isBlank())
            return ResultWrapper.Error(message = "O campo 'Nome' é obrigatório")
        if (description.isBlank())
            return ResultWrapper.Error(message = "O campo 'Descrição' é obrigatório")

        return repository.updateCategory(id, name.trim(), description.trim(), companyId)
    }
}