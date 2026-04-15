package br.com.claus.sellvia.features.company.domain.repository

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.company.data.model.CompanyRequest
import br.com.claus.sellvia.features.company.domain.model.Company

interface ICompanyRepository {
    suspend fun findById(id: Long): ResultWrapper<Company>
    suspend fun update(id: Long, request: CompanyRequest): ResultWrapper<Company>
    suspend fun updateImage(id: Long, imageBytes: ByteArray): ResultWrapper<Company>
}
