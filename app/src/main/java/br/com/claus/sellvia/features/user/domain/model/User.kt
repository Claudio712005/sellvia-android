package br.com.claus.sellvia.features.user.domain.model

import br.com.claus.sellvia.core.model.UserRole

data class User(
    val id: Long,
    val username: String,
    val name: String,
    val role: UserRole,
    val email: String,
)
