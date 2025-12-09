package iolite.network

import iolite.ValueObject

/**
 * Exact Domain Name (for FQDN)
 */
@JvmInline
value class Domain(private val value: String) : ValueObject<String> {

    override fun parse(): String {
        val normalized = value.trim()
        require(
            Regex("^([a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}\$").matches(normalized)
        ) {
            "Invalid Domain: $value"
        }
        return normalized
    }
}
