package br.com.claus.sellvia.ui.components.paginationTemplate.models

import br.com.claus.sellvia.core.model.Direction

open class SearchQuery(
    val page: Int = 0,
    val perPage: Int = 10,
    val terms: String = "",
    val sort: String = "id",
    val direction: Direction = Direction.ASC
)