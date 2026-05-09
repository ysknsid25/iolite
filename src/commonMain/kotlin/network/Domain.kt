package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Fully-qualified domain name (FQDN) with at least one dot and a real TLD.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - Two or more labels separated by `.`. Each label is 1–63 characters of alphanumerics
 *   plus hyphens, but must not start or end with a hyphen.
 * - The final label (TLD) must be at least two alphabetic characters
 *   (digits and hyphens are not permitted in the TLD).
 * - Single-label names such as `localhost` are rejected — use [HostName] for those.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The domain is preserved (no case folding).
 *
 * ```kotlin
 * Domain("example.com").parse()      // → "example.com"
 * Domain("sub.example.co.jp").parse()
 * Domain("localhost").parse()        // throws IoliteException (Format) — no TLD
 * ```
 */
@JvmInline
value class Domain(private val value: String) : ValueObject<String> {

    /**
     * Validates the wrapped domain and returns the trimmed form.
     *
     * @return the trimmed domain string.
     * @throws IoliteException with [target = Domain][IoliteException.Target.Domain]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         a well-formed FQDN.
     */
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Domain,
            rule = IoliteException.Rule.Format,
            condition = Regex("^([a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}\$").matches(normalized),
        ) {
            "Invalid Domain: $value"
        }
        return normalized
    }

    /**
     * Returns `Domain(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "Domain($value)"
}
