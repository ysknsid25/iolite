package iolite.id

import iolite.ValueObject
import kotlin.jvm.JvmInline

@JvmInline
value class Uuid(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        require(
            Regex(
                "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\$"
            ).matches(normalized)
        ) {
            "Invalid UUID: $value"
        }
        return normalized
    }
}
