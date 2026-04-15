package br.com.claus.sellvia.features.company.data

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.network.mapSuccess
import br.com.claus.sellvia.core.network.safeApiCall
import br.com.claus.sellvia.core.network.safeDirectCall
import br.com.claus.sellvia.features.company.data.mapper.toDomain
import br.com.claus.sellvia.features.company.data.model.CompanyRequest
import br.com.claus.sellvia.features.company.domain.model.Company
import br.com.claus.sellvia.features.company.domain.repository.ICompanyRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class CompanyRepositoryImpl(
    private val service: CompanyService,
) : ICompanyRepository {

    override suspend fun findById(id: Long): ResultWrapper<Company> =
        safeDirectCall { service.findById(id) }.mapSuccess { it.toDomain() }

    override suspend fun update(id: Long, request: CompanyRequest): ResultWrapper<Company> =
        safeApiCall { service.update(id, request) }.mapSuccess { it.toDomain() }

    override suspend fun updateImage(id: Long, imageBytes: ByteArray): ResultWrapper<Company> {
        val requestBody = imageBytes.toRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("image", "logo.jpg", requestBody)
        return safeApiCall { service.updateImage(id, part) }.mapSuccess { it.toDomain() }
    }
}
