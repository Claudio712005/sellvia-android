package br.com.claus.sellvia.core.model

enum class Direction {
    ASC, DESC;

    companion object {
        fun fromString(value: String): Direction {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: ASC
        }
    }
}