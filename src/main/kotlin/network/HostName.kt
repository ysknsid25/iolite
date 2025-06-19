package iolite.network

import iolite.ValueObject

/**
 * Any hostname (including localhost and internal names)
 */
@JvmInline
value class HostName(private val value: String) : ValueObject<String> {

    override fun parse(): String {
        val normalized = value.trim()
        require(
            Regex("^([a-zA-Z0-9-]+\\.)*[a-zA-Z0-9-]+\$").matches(normalized)
        ) {
            "Invalid HostName: $value"
        }
        return normalized
    }
}
