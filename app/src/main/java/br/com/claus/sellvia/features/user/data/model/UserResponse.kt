package br.com.claus.sellvia.features.user.data.model

import br.com.claus.sellvia.core.model.UserRole

data class UserResponse(
    val id: Long,
    val username: String,
    val name: String,
    val role: UserRole,
    val email: String = "",
)
