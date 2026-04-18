package br.com.claus.sellvia.features.user.data.model

import br.com.claus.sellvia.core.model.UserRole

data class UserResponse(
    val id: Long? = null,
    val username: String? = null,
    val name: String? = null,
    val role: UserRole? = null,
    val email: String? = null,
)
