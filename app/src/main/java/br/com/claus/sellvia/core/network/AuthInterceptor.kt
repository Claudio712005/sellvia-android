package br.com.claus.sellvia.core.network

import br.com.claus.sellvia.core.data.storage.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(tokenManager: TokenManager) : Interceptor {

    private var cachedToken: String? = null

    init {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            tokenManager.accessToken.collect { cachedToken = it }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = cachedToken?.let { token ->
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } ?: chain.request()

        return chain.proceed(request)
    }
}