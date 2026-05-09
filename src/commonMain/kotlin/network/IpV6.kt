package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * IPv6 address, including the `::` zero-compression form and IPv4-embedded variants.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - Full form: eight groups of 1–4 hex digits separated by `:`
 *   (e.g. `2001:0db8:85a3:0000:0000:8a2e:0370:7334`).
 * - Compressed form: `::` standing in for one or more groups of zeros
 *   (e.g. `2001:db8::1`, `::1`, `::`).
 * - IPv4-embedded form: trailing dotted-decimal IPv4 portion
 *   (e.g. `::ffff:192.168.0.1`).
 * - CIDR suffixes (`/64`) are not accepted here — use [Cidr] for those.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The address itself is preserved
 *   (no case folding, no canonical compression).
 *
 * ```kotlin
 * IpV6("2001:db8::1").parse()             // → "2001:db8::1"
 * IpV6("::ffff:192.168.0.1").parse()
 * ```
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4291">RFC 4291 — IPv6 Addressing Architecture</a>
 */
@JvmInline
value class IpV6(private val value: String) : ValueObject<String> {

    /**
     * Validates the wrapped IPv6 address and returns the trimmed form.
     *
     * @return the trimmed IPv6 address.
     * @throws IoliteException with [target = IpV6][IoliteException.Target.IpV6]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         a well-formed IPv6 address.
     */
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.IpV6,
            rule = IoliteException.Rule.Format,
            condition = ipv6Regex.matches(normalized),
        ) {
            "Invalid IPv6 Address: $value"
        }
        return normalized
    }

    /**
     * Returns `IpV6(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "IpV6($value)"

    companion object {
        private fun ipv4Part(): String {
            val byte = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)"
            return "($byte\\.){3}$byte"
        }

        private val ipv6Regex = Regex(
            "^(" +
                "([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,7}:|" +
                "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
                "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|" +
                "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|" +
                "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|" +
                ":((:[0-9a-fA-F]{1,4}){1,7}|:)|" +
                // IPv4 embedded variations
                "([0-9a-fA-F]{1,4}:){6}${ipv4Part()}|" +
                "([0-9a-fA-F]{1,4}:){5}:${ipv4Part()}|" +
                "([0-9a-fA-F]{1,4}:){4}(:[0-9a-fA-F]{1,4})?:?${ipv4Part()}|" +
                "([0-9a-fA-F]{1,4}:){3}(:[0-9a-fA-F]{1,4}){0,2}:?${ipv4Part()}|" +
                "([0-9a-fA-F]{1,4}:){2}(:[0-9a-fA-F]{1,4}){0,3}:?${ipv4Part()}|" +
                "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){0,4})?:?${ipv4Part()}|" +
                ":((:[0-9a-fA-F]{1,4}){0,5})?:?${ipv4Part()}" +
                ")$"
        )
    }
}
