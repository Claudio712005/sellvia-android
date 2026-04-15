package br.com.claus.sellvia.core.network

import com.google.gson.Gson
import kotlinx.serialization.Serializable
import okhttp3.ResponseBody

@Serializable
data class ApiErrorResponse(
    val message: String? = null,
    val status: Int? = null,
    val error: String? = null
)

fun ApiErrorResponse.Companion.parseErrorBody(errorBody: ResponseBody?): String {
    return try {
        val json = errorBody?.string()
        val gson = Gson()
        val errorResponse = gson.fromJson(json, ApiErrorResponse::class.java)
        errorResponse.message ?: "Erro desconhecido"
    } catch (e: Exception) {
        "Erro ao processar resposta do servidor"
    }
}