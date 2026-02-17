package br.com.claus.sellvia.core.ui.extensions

fun Long.toItemLabel(): String =
    if (this == 1L) "$this item" else "$this items"