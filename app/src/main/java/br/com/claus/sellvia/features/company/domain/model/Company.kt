package br.com.claus.sellvia.features.company.domain.model

data class Company(
    val id: Long,
    val name: String,
    val websiteUrl: String,
    val logoUrl: String,
    val mainPhoneNumber: String,
)
