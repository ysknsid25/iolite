package iolite.datetime

import iolite.ValueObject

@JvmInline
value class Date(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        require(
            dateRegex.matches(normalized)
        ) {
            "Invalid date format: $value"
        }
        return normalized
    }

    companion object {
        @Suppress("MaxLineLength")
        const val DATE_REGEX_SOURCE = """((\d\d[2468][048]|\d\d[13579][26]|\d\d0[48]|[02468][048]00|[13579][26]00)-02-29|\d{4}-((0[13578]|1[02])-(0[1-9]|[12]\d|3[01])|(0[469]|11)-(0[1-9]|[12]\d|30)|(02)-(0[1-9]|1\d|2[0-8])))"""
        private val dateRegex = Regex("^$DATE_REGEX_SOURCE$")
    }
}
