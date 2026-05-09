package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * CIDR notation block covering both IPv4 and IPv6.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - IPv4 CIDR: a valid IPv4 address followed by `/` and a prefix length in `0`–`32`
 *   (e.g. `192.168.0.0/24`).
 * - IPv6 CIDR: a valid IPv6 address (any of the forms accepted by [IpV6]) followed by
 *   `/` and a prefix length in `0`–`128` (e.g. `2001:db8::/32`).
 *
 * Use [isV4] / [isV6] after a successful [parse] to discriminate which family
 * the value represents.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The address and prefix are preserved.
 *
 * ```kotlin
 * val cidr = Cidr("192.168.0.0/24")
 * cidr.parse()    // → "192.168.0.0/24"
 * cidr.isV4()     // true
 * cidr.isV6()     // false
 * ```
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4632">RFC 4632 — CIDR</a>
 */
@JvmInline
public value class Cidr(private val value: String) : ValueObject<String> {

    /**
     * Validates the wrapped CIDR notation (either IPv4 or IPv6) and returns the trimmed form.
     *
     * @return the trimmed CIDR string.
     * @throws IoliteException with [target = Cidr][IoliteException.Target.Cidr]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         a well-formed IPv4 or IPv6 CIDR block.
     */
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Cidr,
            rule = IoliteException.Rule.Format,
            condition = cidrRegexV4.matches(normalized) || cidrRegexV6.matches(normalized),
        ) {
            "Invalid CIDR notation: $value"
        }
        return normalized
    }

    /**
     * Returns `true` when the wrapped value (after trimming) parses as an IPv4 CIDR.
     *
     * Does not throw — usable both before and after [parse].
     */
    public fun isV4(): Boolean {
        return cidrRegexV4.matches(value.trim())
    }

    /**
     * Returns `true` when the wrapped value (after trimming) parses as an IPv6 CIDR.
     *
     * Does not throw — usable both before and after [parse].
     */
    public fun isV6(): Boolean {
        return cidrRegexV6.matches(value.trim())
    }

    /**
     * Returns `Cidr(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "Cidr($value)"

    private companion object {
        @Suppress("MaxLineLength")
        private val cidrRegexV4 = Regex(
            "^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\/(3[0-2]|[12]?[0-9])\$"
        )

        @Suppress("MaxLineLength")
        private val cidrRegexV6 =
            Regex(
                "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))\\/(12[0-8]|1[01][0-9]|[1-9]?[0-9])\$"
            )
    }
}
