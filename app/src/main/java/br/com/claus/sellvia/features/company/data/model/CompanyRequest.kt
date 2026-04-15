package br.com.claus.sellvia.features.company.data.model

data class CompanyRequest(
    val id: Long,
    val name: String,
    val websiteUrl: String = "",
    val mainPhoneNumber: String = "",
    val isActive: Boolean = true,
)
