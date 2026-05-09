package iolite.id

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * UUID in the canonical 8-4-4-4-12 hyphenated string form.
 *
 * Accepts (after surrounding whitespace is trimmed, case-insensitive):
 * - 32 hex digits split into groups of 8-4-4-4-12 separated by hyphens
 *   (e.g. `123e4567-e89b-12d3-a456-426614174000`).
 * - Any UUID version (v1–v8) and any variant — version / variant bits are not enforced.
 * - Forms without hyphens, with braces, or as URN (`urn:uuid:…`) are not accepted.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. Case is preserved (no lower-casing).
 *
 * ```kotlin
 * Uuid("123e4567-e89b-12d3-a456-426614174000").parse()
 * // → "123e4567-e89b-12d3-a456-426614174000"
 * ```
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4122">RFC 4122 — UUID</a>
 */
@JvmInline
value class Uuid(private val value: String) : ValueObject<String> {
    /**
     * Validates the wrapped value as a canonical UUID string and returns the trimmed form.
     *
     * @return the trimmed UUID string (case preserved).
     * @throws IoliteException with [target = Uuid][IoliteException.Target.Uuid]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         a canonical 8-4-4-4-12 hyphenated UUID.
     */
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Uuid,
            rule = IoliteException.Rule.Format,
            condition = Regex(
                "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\$"
            ).matches(normalized),
        ) {
            "Invalid UUID: $value"
        }
        return normalized
    }

    /**
     * Returns `Uuid(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "Uuid($value)"
}
