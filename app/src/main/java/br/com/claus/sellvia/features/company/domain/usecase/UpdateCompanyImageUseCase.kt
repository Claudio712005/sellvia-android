package br.com.claus.sellvia.features.company.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.company.domain.model.Company
import br.com.claus.sellvia.features.company.domain.repository.ICompanyRepository

class UpdateCompanyImageUseCase(private val repository: ICompanyRepository) {
    suspend operator fun invoke(id: Long, imageBytes: ByteArray): ResultWrapper<Company> =
        repository.updateImage(id, imageBytes)
}
