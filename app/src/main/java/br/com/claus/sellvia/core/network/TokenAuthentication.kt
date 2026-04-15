package br.com.claus.sellvia.core.network

import br.com.claus.sellvia.core.data.storage.TokenManager
import br.com.claus.sellvia.features.login.data.LoginService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.atomic.AtomicBoolean

class TokenAuthenticator(
    private val tokenManager: TokenManager,
    private val loginService: LoginService
) : Authenticator {

    private val isRefreshing = AtomicBoolean(false)

    override fun authenticate(route: Route?, response: Response): Request? {

        if (responseCount(response) >= 2) return null

        val refreshToken = runBlocking { tokenManager.refreshToken.firstOrNull() }
            ?: return null

        if (!isRefreshing.compareAndSet(false, true)) {
            return null
        }

        return try {

            val refreshResponse =
                loginService.refreshToken("Bearer $refreshToken").execute()

            if (refreshResponse.isSuccessful && refreshResponse.body() != null) {

                val newTokens = refreshResponse.body()!!

                runBlocking {
                    tokenManager.saveTokens(
                        newTokens.token,
                        newTokens.refreshToken,
                        newTokens.company.id ?: 0L,
                        newTokens.user.id ?: 0L,
                        newTokens.user.role
                    )
                }

                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.token}")
                    .build()

            } else {
                runBlocking { tokenManager.clearTokens() }
                null
            }

        } finally {
            isRefreshing.set(false)
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var current = response.priorResponse
        while (current != null) {
            result++
            current = current.priorResponse
        }
        return result
    }
}
