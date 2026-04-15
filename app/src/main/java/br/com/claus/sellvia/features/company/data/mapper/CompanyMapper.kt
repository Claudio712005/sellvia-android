package br.com.claus.sellvia.features.company.data.mapper

import br.com.claus.sellvia.features.company.data.model.CompanyResponse
import br.com.claus.sellvia.features.company.domain.model.Company

fun CompanyResponse.toDomain() = Company(
    id = id ?: 0L,
    name = name,
    websiteUrl = websiteUrl,
    logoUrl = companyUrlLogo,
    mainPhoneNumber = mainPhoneNumber,
)
