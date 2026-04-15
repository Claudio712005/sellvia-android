package br.com.claus.sellvia.features.company.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.company.data.model.CompanyRequest
import br.com.claus.sellvia.features.company.domain.model.Company
import br.com.claus.sellvia.features.company.domain.repository.ICompanyRepository

class UpdateCompanyUseCase(private val repository: ICompanyRepository) {
    suspend operator fun invoke(id: Long, request: CompanyRequest): ResultWrapper<Company> =
        repository.update(id, request)
}
