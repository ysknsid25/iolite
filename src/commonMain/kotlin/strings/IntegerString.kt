package iolite.strings

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * String representation of an integer, with optional sign.
 *
 * Accepts:
 * - An optional leading `+` or `-`.
 * - Either a single `0`, or a non-zero leading digit followed by zero or more digits.
 *   Leading zeroes other than `"0"` itself are rejected (e.g. `"007"` is invalid).
 *
 * Normalization: none — the wrapped value is returned wrapped in a [StringValueObject]
 * for further fluent validation.
 *
 * ```kotlin
 * val n: String = IntegerString("-42").parse().parse()  // → "-42"
 * IntegerString("3.14").parse()                          // throws IoliteException (Format)
 * IntegerString("007").parse()                           // throws IoliteException (Format)
 * ```
 */
@JvmInline
value class IntegerString(private val value: String) : ValueObject<StringValueObject> {

    /**
     * Validates the wrapped value as an integer string and wraps it for further chaining.
     *
     * @return a [StringValueObject] holding the original (un-normalized) integer string,
     *         allowing further fluent validation.
     * @throws IoliteException with [target = IntegerString][IoliteException.Target.IntegerString]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not a
     *         well-formed integer string.
     */
    override fun parse(): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.IntegerString,
            rule = IoliteException.Rule.Format,
            condition = Regex("^[+-]?(0|[1-9][0-9]*)\$").matches(value),
        ) {
            "Invalid Integer String: $value"
        }
        return StringValueObject(value)
    }

    /**
     * Returns `IntegerString(value)`. The format is **not** part of the public
     * API contract and may change.
     */
    override fun toString(): String = "IntegerString($value)"
}
