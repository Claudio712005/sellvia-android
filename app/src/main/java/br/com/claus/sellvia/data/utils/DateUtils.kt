package br.com.claus.sellvia.data.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDate(date: LocalDateTime): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        date.format(formatter)
    } catch (e: Exception) {
        ""
    }
}