package br.com.claus.sellvia.core.ui

sealed class UIEvent {
    object SaveSuccess : UIEvent()
    object DeleteSuccess : UIEvent()
    data class Error(val message: String) : UIEvent()
    data class Navigation(val route: String) : UIEvent()
}