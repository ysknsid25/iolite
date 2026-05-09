package iolite.strings

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * String containing only ASCII alphanumeric characters.
 *
 * Accepts:
 * - One or more characters from `[A-Za-z0-9]`. Empty strings are rejected.
 * - No whitespace, punctuation, or non-ASCII letters.
 *
 * Normalization: none — the wrapped value is returned wrapped in a [StringValueObject]
 * for further fluent validation.
 *
 * ```kotlin
 * val s: String = AlphaNumericString("abc123").parse().parse()  // → "abc123"
 * AlphaNumericString("abc-123").parse()                          // throws IoliteException (Format)
 * AlphaNumericString("").parse()                                  // throws IoliteException (Format)
 * ```
 */
@JvmInline
public value class AlphaNumericString(private val value: String) : ValueObject<StringValueObject> {

    /**
     * Validates the wrapped value as an alphanumeric string and wraps it for further chaining.
     *
     * @return a [StringValueObject] holding the original (un-normalized) alphanumeric string,
     *         allowing further fluent validation.
     * @throws IoliteException with [target = AlphaNumericString][IoliteException.Target.AlphaNumericString]
     *         and [rule = Format][IoliteException.Rule.Format] if the value contains
     *         characters other than ASCII letters / digits, or is empty.
     */
    override fun parse(): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.AlphaNumericString,
            rule = IoliteException.Rule.Format,
            condition = Regex("^[a-zA-Z0-9]+$").matches(value),
        ) {
            "Invalid Alphanumeric String: $value"
        }
        return StringValueObject(value)
    }

    /**
     * Returns `AlphaNumericString(value)`. The format is **not** part of the
     * public API contract and may change.
     */
    override fun toString(): String = "AlphaNumericString($value)"
}
