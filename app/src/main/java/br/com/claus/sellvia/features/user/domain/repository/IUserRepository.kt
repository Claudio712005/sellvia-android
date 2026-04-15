package br.com.claus.sellvia.features.user.domain.repository

import br.com.claus.sellvia.features.user.data.model.UserRequest
import br.com.claus.sellvia.features.user.data.model.UserResponse

interface IUserRepository {

    fun getById(id: Long): UserResponse

    fun save(user: UserRequest)

    fun delete(id: Long)

    fun update(user: UserRequest): UserResponse

}