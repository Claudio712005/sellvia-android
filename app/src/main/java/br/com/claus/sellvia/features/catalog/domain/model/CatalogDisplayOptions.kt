package br.com.claus.sellvia.features.catalog.domain.model

import br.com.claus.sellvia.core.model.CardStyle

data class CatalogDisplayOptions(
    val columnsPerRow: Int = 2,
    val cardStyle: CardStyle = CardStyle.CUSTOMER,
    val showProductImages: Boolean = true,
    val showCompanyLogo: Boolean = true,
    val showCompanyBusinessName: Boolean = true,
    val showCompanyCnpj: Boolean = true,
    val showCompanyWebsite: Boolean = true,
    val showStats: Boolean = true,
    val showAveragePrice: Boolean = true,
    val showActiveCount: Boolean = true,
    val showTypeBreakdown: Boolean = true,
    val showSku: Boolean = true,
    val showStock: Boolean = true,
    val showProductionCost: Boolean = false,
    val showCategory: Boolean = true,
    val showDescription: Boolean = true,
    val showStatus: Boolean = true,
)
