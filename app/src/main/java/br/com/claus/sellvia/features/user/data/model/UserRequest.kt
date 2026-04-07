package br.com.claus.sellvia.features.user.data.model

import br.com.claus.sellvia.core.model.UserRole

data class UserRequest (
    val id: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val userRole: UserRole? = null,
    val companyId: Long? = null,
    val name: String? = null,
    val cpf: String? = null,
){

}