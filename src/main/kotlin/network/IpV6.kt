package iolite.network

import iolite.ValueObject

@JvmInline
value class IpV6(private val value: String) : ValueObject<String> {

    override fun parse(): String {
        val normalized = value.trim()
        require(ipv6Regex.matches(normalized)) {
            "Invalid IPv6 Address: $value"
        }
        return normalized
    }

    companion object {
        private fun ipv4Part(): String {
            val byte = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)"
            return "($byte\\.){3}$byte"
        }

        // Regex components for IPv6 validation based on RFC 4291
        private val fullAddress = "([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}"
        private val compressedAddress = "([0-9a-fA-F]{1,4}:){1,7}:"
        private val mixedAddress = "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}"
        private val ipv4Embedded = listOf(
            "([0-9a-fA-F]{1,4}:){6}${ipv4Part()}",
            "([0-9a-fA-F]{1,4}:){5}:${ipv4Part()}",
            "([0-9a-fA-F]{1,4}:){4}(:[0-9a-fA-F]{1,4})?:?${ipv4Part()}",
            "([0-9a-fA-F]{1,4}:){3}(:[0-9a-fA-F]{1,4}){0,2}:?${ipv4Part()}",
            "([0-9a-fA-F]{1,4}:){2}(:[0-9a-fA-F]{1,4}){0,3}:?${ipv4Part()}",
            "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){0,4})?:?${ipv4Part()}",
            ":((:[0-9a-fA-F]{1,4}){0,5})?:?${ipv4Part()}"
        ).joinToString("|")

        private val ipv6Regex = Regex(
            "^($fullAddress|$compressedAddress|$mixedAddress|$ipv4Embedded)$"
        )
    }
}
