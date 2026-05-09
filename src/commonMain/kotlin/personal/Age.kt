package iolite.personal

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Human age expressed as an integer year count.
 *
 * Accepts:
 * - Any integer in the inclusive range [[MIN_AGE][Age.Companion.MIN_AGE], [MAX_AGE][Age.Companion.MAX_AGE]].
 *
 * Normalization: none — the value is returned as-is.
 *
 * ```kotlin
 * val years: Int = Age(34).parse()  // → 34
 * Age(-1).parse()                   // throws IoliteException (Range)
 * Age(500).parse()                  // throws IoliteException (Range)
 * ```
 */
@JvmInline
value class Age(private val value: Int) : ValueObject<Int> {

    /**
     * Validates the wrapped age and returns it unchanged.
     *
     * @return the age value (no normalization is applied).
     * @throws IoliteException with [target = Age][IoliteException.Target.Age] and
     *         [rule = Range][IoliteException.Rule.Range] if the value is outside
     *         `[MIN_AGE, MAX_AGE]`.
     */
    override fun parse(): Int {
        ioliteRequire(
            target = IoliteException.Target.Age,
            rule = IoliteException.Rule.Range,
            condition = value in MIN_AGE..MAX_AGE,
        ) {
            "Invalid age. Age must be between $MIN_AGE and $MAX_AGE."
        }
        return value
    }

    /**
     * Returns `Age(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "Age($value)"

    companion object {
        /** Minimum accepted age (inclusive). */
        const val MIN_AGE = 0

        /** Maximum accepted age (inclusive). */
        const val MAX_AGE = 200 // I hope this number keeps getting bigger
    }
}
