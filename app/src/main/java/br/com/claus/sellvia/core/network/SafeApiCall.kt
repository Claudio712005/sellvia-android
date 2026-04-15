package br.com.claus.sellvia.core.network

import br.com.claus.sellvia.core.network.ApiErrorResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): ResultWrapper<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            @Suppress("UNCHECKED_CAST")
            ResultWrapper.Success(response.body() ?: Unit as T)
        } else {
            val message = ApiErrorResponse.parseErrorBody(response.errorBody())
            ResultWrapper.Error(code = response.code(), message = message)
        }
    } catch (e: IOException) {
        ResultWrapper.Error(message = "Sem conexão com a internet")
    } catch (e: Exception) {
        ResultWrapper.Error(message = e.message ?: "Erro inesperado")
    }
}

suspend fun <T> safeDirectCall(call: suspend () -> T): ResultWrapper<T> {
    return try {
        ResultWrapper.Success(call())
    } catch (e: HttpException) {
        ResultWrapper.Error(code = e.code(), message = e.message() ?: "Erro desconhecido")
    } catch (e: IOException) {
        ResultWrapper.Error(message = "Sem conexão com a internet")
    } catch (e: Exception) {
        ResultWrapper.Error(message = e.message ?: "Erro inesperado")
    }
}