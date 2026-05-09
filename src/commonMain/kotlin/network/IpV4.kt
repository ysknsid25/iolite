package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * IPv4 address in dotted-decimal notation.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - Four decimal octets separated by `.`, each in the range `0`–`255`.
 *   Leading zeroes within an octet (e.g. `192.168.001.001`) are rejected.
 * - CIDR suffixes (`/24`) are not accepted here — use [Cidr] for those.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The address itself is preserved.
 *
 * ```kotlin
 * IpV4("192.168.0.1").parse()  // → "192.168.0.1"
 * IpV4("256.0.0.1").parse()    // throws IoliteException (Format)
 * ```
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc791">RFC 791 — Internet Protocol</a>
 */
@JvmInline
value class IpV4(private val value: String) : ValueObject<String> {

    /**
     * Validates the wrapped IPv4 address and returns the trimmed form.
     *
     * @return the trimmed IPv4 address.
     * @throws IoliteException with [target = IpV4][IoliteException.Target.IpV4]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         a well-formed IPv4 address.
     */
    @Suppress("MaxLineLength")
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.IpV4,
            rule = IoliteException.Rule.Format,
            condition = ipv4Regex.matches(normalized),
        ) {
            "Invalid IPV4 Address: $value"
        }
        return normalized
    }

    /**
     * Returns `IpV4(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "IpV4($value)"

    companion object {
        @Suppress("MaxLineLength")
        private val ipv4Regex = Regex(
            "^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\$"
        )
    }
}
