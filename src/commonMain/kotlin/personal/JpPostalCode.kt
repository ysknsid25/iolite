package iolite.personal

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Japanese postal code (郵便番号) in 7-digit form.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - 7 digits with no separator (e.g. `1000001`).
 * - 7 digits with a single hyphen between the 3rd and 4th digit (e.g. `100-0001`).
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The hyphen, if present, is preserved.
 *
 * ```kotlin
 * val zip: String = JpPostalCode("100-0001").parse()  // → "100-0001"
 * val zip2: String = JpPostalCode("1000001").parse()  // → "1000001"
 * ```
 */
@JvmInline
value class JpPostalCode(private val value: String) : ValueObject<String> {
    /**
     * Validates the wrapped postal code and returns the trimmed form.
     *
     * @return the trimmed postal code (hyphen preserved as written, if present).
     * @throws IoliteException with [target = JpPostalCode][IoliteException.Target.JpPostalCode]
     *         and [rule = Format][IoliteException.Rule.Format] if the value does not match
     *         the accepted postal-code pattern.
     */
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.JpPostalCode,
            rule = IoliteException.Rule.Format,
            condition = Regex("""^\d{3}-?\d{4}$""").matches(normalized),
        ) {
            "Invalid Japanese Postal Code"
        }
        return normalized
    }

    /**
     * Returns `JpPostalCode(value)`. The format is **not** part of the public
     * API contract and may change.
     */
    override fun toString(): String = "JpPostalCode($value)"
}
