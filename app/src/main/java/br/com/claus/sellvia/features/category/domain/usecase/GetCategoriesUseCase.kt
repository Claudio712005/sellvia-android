package br.com.claus.sellvia.features.category.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.model.Direction
import br.com.claus.sellvia.features.category.domain.model.Category
import br.com.claus.sellvia.features.category.domain.repository.ICategoryRepository
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination

class GetCategoriesUseCase(private val repository: ICategoryRepository) {

    suspend operator fun invoke(
        page: Int = 0,
        perPage: Int = 10,
        sort: String = "name",
        sortDirection: Direction = Direction.ASC,
        companyId: Long?,
        name: String? = null
    ): ResultWrapper<Pagination<Category>> =
        repository.getCategories(
            page = page,
            perPage = perPage,
            sort = sort,
            sortDirection = sortDirection,
            companyId = companyId,
            name = name
        )
}