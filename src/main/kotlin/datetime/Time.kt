package iolite.datetime

import iolite.ValueObject

class Time(private val value: String, private val precision: Int? = null) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        require(
            Regex(
                "^${timeRegexSource(precision)}\$"
            ).matches(normalized)
        ) {
            "Invalid time format: $value"
        }
        return normalized
    }

    companion object {
        fun timeRegexSource(precision: Int? = null): String {
            val prefix = "[0-5]\\d"
            val secondsRegexSource = if (precision != null) {
                "$prefix\\.\\d{$precision}"
            } else {
                "$prefix(\\.\\d+)?"
            }
            val secondsQuantifier = if (precision != null) "+" else "?"
            return "([01]\\d|2[0-3]):[0-5]\\d(:$secondsRegexSource)$secondsQuantifier"
        }
    }
}
