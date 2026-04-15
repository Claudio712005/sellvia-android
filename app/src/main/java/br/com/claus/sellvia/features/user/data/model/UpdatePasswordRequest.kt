package br.com.claus.sellvia.features.user.data.model

data class UpdatePasswordRequest(
    val password: String,
    val confirmPassword: String,
    val newPassword: String,
)
