package br.com.claus.sellvia.features.user.data.mapper

import br.com.claus.sellvia.features.user.data.model.UserResponse
import br.com.claus.sellvia.features.user.domain.model.User
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination

fun UserResponse.toDomain() = User(
    id = id,
    username = username,
    name = name,
    role = role,
    email = email,
)

fun Pagination<UserResponse>.toDomain(): Pagination<User> = Pagination(
    items = items.map { it.toDomain() },
    currentPage = currentPage,
    perPage = perPage,
    totalPages = totalPages,
    totalItems = totalItems,
)
