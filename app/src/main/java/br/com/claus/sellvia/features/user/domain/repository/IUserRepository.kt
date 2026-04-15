package br.com.claus.sellvia.features.user.domain.repository

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.user.data.model.UpdatePasswordRequest
import br.com.claus.sellvia.features.user.domain.model.User

interface IUserRepository {

    suspend fun findById(id: Long): ResultWrapper<User>

    suspend fun updatePassword(id: Long, request: UpdatePasswordRequest): ResultWrapper<String>
}
