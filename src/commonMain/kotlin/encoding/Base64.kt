package iolite.encoding

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Base64-encoded string in the standard alphabet (RFC 4648 §4).
 *
 * Accepts (after surrounding whitespace is trimmed, case-insensitive):
 * - Zero or more groups of 4 characters from `[A-Za-z0-9+/]`.
 * - Optional final padding group of either `XX==` or `XXX=` to complete a 4-character chunk.
 * - The empty string is accepted (it encodes empty bytes).
 * - URL-safe Base64 (`-` / `_` instead of `+` / `/`) is **not** accepted.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The encoding itself is preserved (no case folding).
 *
 * ```kotlin
 * Base64("aGVsbG8=").parse()  // → "aGVsbG8="
 * Base64("not base64!").parse() // throws IoliteException (Format)
 * ```
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-4">RFC 4648 §4 — Base64</a>
 */
@JvmInline
public value class Base64(private val value: String) : ValueObject<String> {
    /**
     * Validates the wrapped value as standard-alphabet Base64 and returns the trimmed form.
     *
     * @return the trimmed Base64 string.
     * @throws IoliteException with [target = Base64][IoliteException.Target.Base64]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         well-formed standard Base64.
     */
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Base64,
            rule = IoliteException.Rule.Format,
            condition = Regex(
                "^(?:[\\da-z+/]{4})*(?:[\\da-z+/]{2}==|[\\da-z+/]{3}=)?$",
                setOf(RegexOption.IGNORE_CASE)
            ).matches(normalized),
        ) {
            "Invalid Base64 string: $value"
        }
        return normalized
    }

    /**
     * Returns `Base64(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "Base64($value)"
}
