package iolite.personal

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Practical email address, accepting most real-world inputs that loosely follow RFC 5322.
 *
 * Accepts:
 * - Local part: `A-Z`, `a-z`, `0-9`, and `_'+-.`. Leading and trailing dots are forbidden,
 *   and consecutive dots (`..`) are forbidden.
 * - Domain part: each label is alphanumeric with optional hyphens; the TLD must be at
 *   least two alphabetic characters (e.g. `example.com`, `mail.co.jp`).
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`.
 * - The whole address is lowercased via `lowercase()`.
 *
 * ```kotlin
 * val normalized: String = Email("  Alice@Example.COM ").parse()
 * // → "alice@example.com"
 * ```
 *
 * Note: validation errors do **not** echo the input value, since email addresses
 * are PII (see iolite's sensitivity policy).
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc5322">RFC 5322 — Internet Message Format</a>
 */
@JvmInline
value class Email(private val value: String) : ValueObject<String> {

    /**
     * Validates the wrapped email and returns the normalized form
     * (trimmed and lowercased).
     *
     * @return the trimmed, lowercased email address.
     * @throws IoliteException with [target = Email][IoliteException.Target.Email] and
     *         [rule = Format][IoliteException.Rule.Format] if the value does not match
     *         the accepted email pattern. The error message does not echo the input.
     */
    @Suppress("MaxLineLength")
    override fun parse(): String {
        val normalized = value.trim().lowercase()
        ioliteRequire(
            target = IoliteException.Target.Email,
            rule = IoliteException.Rule.Format,
            condition = Regex(
                "^(?!\\.)(?!.*\\.\\.)([A-Za-z0-9_'+\\-\\.]*)[A-Za-z0-9_+\\-]@([A-Za-z0-9][A-Za-z0-9\\-]*\\.)+[A-Za-z]{2,}$"
            ).matches(normalized)
        ) {
            "Invalid email address"
        }
        return normalized
    }

    /**
     * Returns a masked representation: e.g. `Email(j***@example.com)`.
     * The local-part is masked except for its first character so that PII
     * does not leak into logs / debugger output. The exact mask format is
     * **not** part of the public API contract and may change.
     */
    override fun toString(): String {
        val at = value.indexOf('@')
        if (at <= 0) return "Email(***)"
        val local = value.substring(0, at)
        val domain = value.substring(at)
        val masked = local.first() + "***"
        return "Email($masked$domain)"
    }
}
