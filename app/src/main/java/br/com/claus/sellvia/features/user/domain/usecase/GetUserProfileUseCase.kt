package br.com.claus.sellvia.features.user.domain.usecase

import br.com.claus.sellvia.core.network.ResultWrapper
import br.com.claus.sellvia.features.user.domain.model.User
import br.com.claus.sellvia.features.user.domain.repository.IUserRepository

class GetUserProfileUseCase(private val repository: IUserRepository) {

    suspend operator fun invoke(id: Long): ResultWrapper<User> =
        repository.findById(id)
}
