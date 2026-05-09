package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * MAC address (EUI-48 / EUI-64) in any of the common notations.
 *
 * Accepts (after surrounding whitespace is trimmed, case-insensitive):
 * - EUI-48 (6 bytes):
 *     - Colon-separated bytes (`AA:BB:CC:DD:EE:FF`).
 *     - Hyphen-separated bytes (`AA-BB-CC-DD-EE-FF`).
 *     - Cisco-style 4-hex-digit groups separated by `.` (`AABB.CCDD.EEFF`).
 * - EUI-64 (8 bytes):
 *     - Colon- or hyphen-separated bytes (`AA:BB:CC:DD:EE:FF:00:11`).
 *     - 4-hex-digit groups separated by `.` or `:` (`AABB.CCDD.EEFF.0011`, `AABB:CCDD:EEFF:0011`).
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. Case and chosen separator are preserved.
 *
 * ```kotlin
 * MacAddress("aa:bb:cc:dd:ee:ff").parse()  // → "aa:bb:cc:dd:ee:ff"
 * MacAddress("AABB.CCDD.EEFF").parse()
 * ```
 */
@JvmInline
value class MacAddress(private val value: String) : ValueObject<String> {
    /**
     * Validates the wrapped MAC address and returns the trimmed form.
     *
     * @return the trimmed MAC address (case and separator preserved).
     * @throws IoliteException with [target = MacAddress][IoliteException.Target.MacAddress]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         a well-formed MAC address in one of the supported notations.
     */
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
