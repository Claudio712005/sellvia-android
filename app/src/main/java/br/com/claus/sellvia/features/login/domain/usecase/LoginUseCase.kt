package br.com.claus.sellvia.features.login.domain.usecase

import br.com.claus.sellvia.features.login.domain.repository.LoginRepository
import br.com.claus.sellvia.features.login.domain.model.User

class LoginUseCase(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        return repository.login(email, password)
    }
}
