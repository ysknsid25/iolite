package iolite.datetime

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Calendar date in `YYYY-MM-DD` format with proper month and leap-year handling.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - Any 4-digit year, hyphen, 2-digit month, hyphen, 2-digit day-of-month.
 * - Day ranges respect the month: 01–31 for January / March / etc., 01–30 for April / June / etc.,
 *   and 01–28 (or 01–29 in leap years) for February.
 * - Leap year rule applied: divisible by 4, except divisible by 100 unless also divisible by 400.
 * - Out-of-range or non-existent dates (e.g. `2023-02-30`, `2023-13-01`) are rejected.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The date itself is preserved.
 *
 * ```kotlin
 * val d: String = Date("2024-02-29").parse()  // → "2024-02-29"
 * Date("2023-02-29").parse()                  // throws IoliteException (Format) — not a leap year
 * ```
 *
 * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO 8601</a>
 */
@JvmInline
public value class Date(private val value: String) : ValueObject<String> {
    /**
     * Validates the wrapped date and returns the trimmed form.
     *
     * @return the trimmed date string in `YYYY-MM-DD` format.
     * @throws IoliteException with [target = Date][IoliteException.Target.Date]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         a valid `YYYY-MM-DD` date (including leap-year handling).
     */
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Date,
            rule = IoliteException.Rule.Format,
            condition = dateRegex.matches(normalized),
        ) {
            "Invalid date format: $value"
        }
        return normalized
    }

    /**
     * Returns `Date(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "Date($value)"

    public companion object {
        /**
         * Regex source (without surrounding `^…$` anchors) describing a valid
         * `YYYY-MM-DD` calendar date with leap-year handling. Exposed for
         * reuse by [DateTime], which composes this with a time pattern.
         */
        @Suppress("MaxLineLength")
        public const val DATE_REGEX_SOURCE: String = """((\d\d[2468][048]|\d\d[13579][26]|\d\d0[48]|[02468][048]00|[13579][26]00)-02-29|\d{4}-((0[13578]|1[02])-(0[1-9]|[12]\d|3[01])|(0[469]|11)-(0[1-9]|[12]\d|30)|(02)-(0[1-9]|1\d|2[0-8])))"""
        private val dateRegex = Regex("^$DATE_REGEX_SOURCE$")
    }
}
