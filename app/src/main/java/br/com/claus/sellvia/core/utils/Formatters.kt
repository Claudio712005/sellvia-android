package br.com.claus.sellvia.core.utils

fun formatDouble(value: Double): String {
    return if (value == 0.0) "" else value.toString()
}