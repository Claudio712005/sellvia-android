package br.com.claus.sellvia.features.user.data.mapper

import br.com.claus.sellvia.core.model.UserRole
import br.com.claus.sellvia.features.user.data.model.UserResponse
import br.com.claus.sellvia.features.user.domain.model.User
import br.com.claus.sellvia.ui.components.paginationTemplate.models.Pagination

fun UserResponse.toDomain() = User(
    id = id ?: 0L,
    username = username.orEmpty(),
    name = name.orEmpty(),
    role = role ?: UserRole.COMPANY_USER,
    email = email.orEmpty(),
)

fun Pagination<UserResponse>.toDomain(): Pagination<User> = Pagination(
    items = items.orEmpty().map { it.toDomain() },
    currentPage = currentPage,
    perPage = perPage,
    totalPages = totalPages,
    totalItems = totalItems,
)
