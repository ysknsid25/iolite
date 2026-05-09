package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class MacAddress(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.MacAddress,
            rule = IoliteException.Rule.Format,
            condition = macAddressRegex.matches(normalized),
        ) {
            "Invalid MAC Address: $value"
        }
        return normalized
    }

    /**
     * Returns `MacAddress(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "MacAddress($value)"

    companion object {
        @Suppress("MaxLineLength")
        private val macAddressRegex =
            Regex(
                "^(?:[\\da-fA-F]{2}:){5}[\\da-fA-F]{2}$|^(?:[\\da-fA-F]{2}-){5}[\\da-fA-F]{2}$|^(?:[\\da-fA-F]{4}\\.){2}[\\da-fA-F]{4}$|^(?:[\\da-fA-F]{2}:){7}[\\da-fA-F]{2}$|^(?:[\\da-fA-F]{2}-){7}[\\da-fA-F]{2}$|^(?:[\\da-fA-F]{4}\\.){3}[\\da-fA-F]{4}$|^(?:[\\da-fA-F]{4}:){3}[\\da-fA-F]{4}$",
                RegexOption.IGNORE_CASE
            )
    }
}
