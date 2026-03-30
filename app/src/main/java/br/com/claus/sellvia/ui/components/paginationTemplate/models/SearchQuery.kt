package br.com.claus.sellvia.ui.components.paginationTemplate.models

import br.com.claus.sellvia.data.enums.Direction

open class SearchQuery(
    val page: Int = 0,
    val perPage: Int = 10,
    val terms: String = "",
    val sort: String = "id",
    val direction: Direction = Direction.ASC
)