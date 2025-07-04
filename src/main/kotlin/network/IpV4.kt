package iolite.network

import iolite.ValueObject

@JvmInline
value class IpV4(private val value: String) : ValueObject<String> {

    @Suppress("MaxLineLength")
    override fun parse(): String {
        val normalized = value.trim()
        require(
            ipv4Regex.matches(normalized)
        ) {
            "Invalid IPV4 Address: $value"
        }
        return normalized
    }

    companion object {
        @Suppress("MaxLineLength")
        private val ipv4Regex = Regex(
            "^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\$"
        )
    }
}
