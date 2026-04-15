package br.com.claus.sellvia.features.user.data

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.core.network.mapSuccess
import br.com.claus.sellvia.core.network.safeApiCall
import br.com.claus.sellvia.core.network.safeDirectCall
import br.com.claus.sellvia.features.user.data.mapper.toDomain
import br.com.claus.sellvia.features.user.data.model.UpdatePasswordRequest
import br.com.claus.sellvia.features.user.domain.model.User
import br.com.claus.sellvia.features.user.domain.repository.IUserRepository

class UserRepositoryImpl(
    private val service: UserService,
) : IUserRepository {

    override suspend fun findById(id: Long): ResultWrapper<User> =
        safeDirectCall { service.findById(id) }.mapSuccess { it.toDomain() }

    override suspend fun updatePassword(
        id: Long,
        request: UpdatePasswordRequest,
    ): ResultWrapper<String> =
        safeApiCall { service.updatePassword(id, request) }
}
