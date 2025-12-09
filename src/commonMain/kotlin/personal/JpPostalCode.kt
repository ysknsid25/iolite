package iolite.personal

import iolite.ValueObject

@JvmInline
value class JpPostalCode(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        require(
            Regex("""^\d{3}-?\d{4}$""").matches(normalized)
        ) {
            "Invalid Japanese Postal Code: $value"
        }
        return normalized
    }
}
