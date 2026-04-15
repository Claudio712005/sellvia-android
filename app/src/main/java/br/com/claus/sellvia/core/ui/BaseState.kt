package br.com.claus.sellvia.core.ui

interface BaseState {
    val isLoading: Boolean
    val error: String?
    val isSuccess: Boolean
}