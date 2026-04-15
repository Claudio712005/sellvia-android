package br.com.claus.sellvia.features.user.data

import br.com.claus.sellvia.features.user.data.model.UserRequest
import br.com.claus.sellvia.features.user.data.model.UserResponse
import br.com.claus.sellvia.features.user.domain.repository.IUserRepository

class UserRepositoryImpl: IUserRepository{
    override fun getById(id: Long): UserResponse {
        TODO("Not yet implemented")
    }

    override fun save(user: UserRequest) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override fun update(user: UserRequest): UserResponse {
        TODO("Not yet implemented")
    }

}