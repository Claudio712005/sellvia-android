package br.com.claus.sellvia.features.login.data.remote.mock

import br.com.claus.sellvia.features.login.data.remote.LoginApi
import br.com.claus.sellvia.features.login.data.remote.dto.LoginRequest
import br.com.claus.sellvia.features.login.data.remote.dto.LoginResponse
import kotlinx.coroutines.delay

class MockLoginApi : LoginApi {

    override suspend fun login(request: LoginRequest): LoginResponse {
        delay(1500)

        if (request.email == "admin@sellvia.com" && request.password == "123456") {
            return LoginResponse(
                id = 1L,
                name = "Claus",
                email = request.email
            )
        } else {
            throw Exception("Credenciais inválidas")
        }
    }
}
