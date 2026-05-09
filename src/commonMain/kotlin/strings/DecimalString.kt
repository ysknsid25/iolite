package iolite.strings

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * String representation of a decimal number, with optional sign and optional fractional part.
 *
 * Accepts:
 * - An optional leading `+` or `-`.
 * - One or more digits, optionally followed by a single `.` and one or more digits.
 *   `"3"`, `"3.14"`, `"+0.5"`, `"-42"` are all valid.
 *   `"3."` and `".5"` are rejected — both sides of the dot must have at least one digit.
 *
 * Normalization: none — the wrapped value is returned wrapped in a [StringValueObject]
 * for further fluent validation.
 *
 * ```kotlin
 * val n: String = DecimalString("-3.14").parse().parse()  // → "-3.14"
 * DecimalString(".5").parse()                              // throws IoliteException (Format)
 * ```
 */
@JvmInline
public value class DecimalString(private val value: String) : ValueObject<StringValueObject> {

    /**
     * Validates the wrapped value as a decimal string and wraps it for further chaining.
     *
     * @return a [StringValueObject] holding the original (un-normalized) decimal string,
     *         allowing further fluent validation.
     * @throws IoliteException with [target = DecimalString][IoliteException.Target.DecimalString]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not a
     *         well-formed decimal string.
     */
    override fun parse(): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.DecimalString,
            rule = IoliteException.Rule.Format,
            condition = Regex("^[+-]?([0-9]+\\.[0-9]+|[0-9]+)\$").matches(value),
        ) {
            "Invalid Decimal String: $value"
        }
        return StringValueObject(value)
    }

    /**
     * Returns `DecimalString(value)`. The format is **not** part of the public
     * API contract and may change.
     */
    override fun toString(): String = "DecimalString($value)"
}
