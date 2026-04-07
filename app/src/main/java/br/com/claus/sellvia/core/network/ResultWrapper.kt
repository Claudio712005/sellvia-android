package br.com.claus.sellvia.core.network

sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val code: Int? = null, val message: String) : ResultWrapper<Nothing>()
    data object Loading : ResultWrapper<Nothing>()
}

fun <T, R> ResultWrapper<T>.mapSuccess(transform: (T) -> R): ResultWrapper<R> = when (this) {
    is ResultWrapper.Success -> ResultWrapper.Success(transform(data))
    is ResultWrapper.Error -> this
    ResultWrapper.Loading -> ResultWrapper.Loading
}