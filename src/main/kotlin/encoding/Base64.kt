package iolite.encoding

import iolite.ValueObject

@JvmInline
value class Base64(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        require(
            Regex(
                "^(?:[\\da-z+/]{4})*(?:[\\da-z+/]{2}==|[\\da-z+/]{3}=)?$",
                setOf(RegexOption.IGNORE_CASE)
            ).matches(normalized)
        ) {
            "Invalid Base64 string: $value"
        }
        return normalized
    }
}
