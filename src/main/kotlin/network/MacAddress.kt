package iolite.network

import iolite.ValueObject

@JvmInline
value class MacAddress(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        require(macAddressRegex.matches(normalized)) {
            "Invalid MAC Address: $value"
        }
        return normalized
    }

    companion object {
        @Suppress("MaxLineLength")
        val macAddressRegex =
            Regex(
                "^(?:[\\da-fA-F]{2}:){5}[\\da-fA-F]{2}$|^(?:[\\da-fA-F]{2}-){5}[\\da-fA-F]{2}$|^(?:[\\da-fA-F]{4}\\.){2}[\\da-fA-F]{4}$|^(?:[\\da-fA-F]{2}:){7}[\\da-fA-F]{2}$|^(?:[\\da-fA-F]{2}-){7}[\\da-fA-F]{2}$|^(?:[\\da-fA-F]{4}\\.){3}[\\da-fA-F]{4}$|^(?:[\\da-fA-F]{4}:){3}[\\da-fA-F]{4}$",
                RegexOption.IGNORE_CASE
            )
    }
}
