package iolite.datetime

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire

/**
 * Time of day in `HH:MM[:SS[.fraction]]` format with configurable second-fraction precision.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - `HH` in `00`–`23`, `MM` in `00`–`59`. Seconds and fractional digits depend on [precision]:
 *     - `precision = null` (default) — seconds with optional fractional digits are required
 *       (e.g. `12:34:56`, `12:34:56.789`). `12:34` alone is **not** accepted.
 *     - `precision > 0` — seconds with exactly that many fractional digits are required
 *       (e.g. for `precision = 3`, `12:34:56.789` is accepted but `12:34:56.78` is not).
 * - No time-zone suffix is permitted; use [DateTime] for zoned values.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The rest of the string is preserved.
 *
 * ```kotlin
 * Time("12:34:56").parse()                     // → "12:34:56"
 * Time("12:34:56.789", precision = 3).parse()  // → "12:34:56.789"
 * Time("12:34").parse()                        // throws IoliteException (Format) — seconds required
 * ```
 *
 * @property value     the raw time string to validate.
 * @property precision required number of fractional-second digits, or `null` to make them optional.
 *
 * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO 8601</a>
 */
public class Time(private val value: String, private val precision: Int? = null) : ValueObject<String> {
    /**
     * Validates the wrapped time and returns the trimmed form.
     *
     * @return the trimmed time string.
     * @throws IoliteException with [target = Time][IoliteException.Target.Time]
     *         and [rule = Format][IoliteException.Rule.Format] if the value does not
     *         match the pattern implied by the configured [precision].
     */
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Time,
            rule = IoliteException.Rule.Format,
            condition = Regex("^${timeRegexSource(precision)}\$").matches(normalized),
        ) {
            "Invalid time format: $value"
        }
        return normalized
    }

    /**
     * Returns `Time(value=…, precision=…)`. The format is **not** part of the
     * public API contract and may change.
     */
    override fun toString(): String = "Time(value=$value, precision=$precision)"

    public companion object {
        public fun timeRegexSource(precision: Int? = null): String {
            val prefix = "[0-5]\\d"
            val secondsRegexSource = if (precision != null) {
                "$prefix\\.\\d{$precision}"
            } else {
                "$prefix(\\.\\d+)?"
            }
            val secondsQuantifier = if (precision != null) "+" else "?"
            return "([01]\\d|2[0-3]):[0-5]\\d(:$secondsRegexSource)$secondsQuantifier"
        }
    }
}
