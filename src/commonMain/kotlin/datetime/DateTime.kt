package iolite.datetime

import iolite.IoliteException
import iolite.ValueObject
import iolite.datetime.Date.Companion.DATE_REGEX_SOURCE
import iolite.ioliteRequire

/**
 * ISO 8601-style date-time with configurable second-fraction precision and time-zone handling.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - Date portion: a valid `YYYY-MM-DD` calendar date — same rules as [Date],
 *   reusing [Date.DATE_REGEX_SOURCE].
 * - Literal `T` separator, then `HH:MM[:SS[.fraction]]`. The fractional /
 *   seconds requirement depends on [precision]:
 *     - `precision = null` (default) — seconds and fractional digits are both optional.
 *     - `precision = 0` — seconds are optional, no fractional digits permitted.
 *     - `precision > 0` — seconds are required followed by exactly that many fractional digits.
 * - Time-zone suffix depends on [zone]:
 *     - [Zone.UTC] (default) — only the literal `Z` is accepted.
 *     - [Zone.OFFSET] — accepts `Z` or `±HH:MM` / `±HHMM`.
 *     - [Zone.LOCAL] — no zone suffix is permitted.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The rest of the string is preserved.
 *
 * Use one of the factory methods on the companion object for clarity:
 *
 * ```kotlin
 * DateTime.utc("2024-01-02T03:04:05Z").parse()
 * DateTime.utc("2024-01-02T03:04:05.123Z", precision = 3).parse()
 * DateTime.withOffset("2024-01-02T03:04:05+09:00").parse()
 * DateTime.local("2024-01-02T03:04:05").parse()
 * ```
 *
 * @property value     the raw date-time string to validate.
 * @property precision required number of fractional-second digits (see above).
 * @property zone      time-zone suffix policy (see above).
 *
 * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO 8601</a>
 */
@Suppress("ArgumentListWrapping")
public class DateTime(
    private val value: String,
    private val precision: Int? = null,
    private val zone: Zone = Zone.UTC,
) : ValueObject<String> {

    @Deprecated(
        message = "Use DateTime(value, precision, Zone) or factory methods (utc, withOffset, local).",
        replaceWith = ReplaceWith(
            "DateTime(value, precision, " +
                "if (local) DateTime.Zone.LOCAL " +
                "else if (offset) DateTime.Zone.OFFSET " +
                "else DateTime.Zone.UTC)",
        ),
        level = DeprecationLevel.WARNING,
    )
    public constructor(
        value: String,
        precision: Int? = null,
        offset: Boolean,
        local: Boolean = false,
    ) : this(
        value = value,
        precision = precision,
        zone = when {
            local -> Zone.LOCAL
            offset -> Zone.OFFSET
            else -> Zone.UTC
        },
    )

    /**
     * Validates the wrapped date-time and returns the trimmed form.
     *
     * @return the trimmed date-time string.
     * @throws IoliteException with [target = DateTime][IoliteException.Target.DateTime]
     *         and [rule = Format][IoliteException.Rule.Format] if the value does not
     *         match the pattern implied by the configured [precision] and [zone].
     */
    override fun parse(): String {
        val normalized = value.trim()
        val regex = buildRegex()
        ioliteRequire(
            target = IoliteException.Target.DateTime,
            rule = IoliteException.Rule.Format,
            condition = regex.matches(normalized),
        ) {
            "Invalid datetime format: '$value' for precision: $precision, zone: $zone"
        }
        return normalized
    }

    /**
     * Returns `DateTime(value=…, precision=…, zone=…)`. The format is **not**
     * part of the public API contract and may change.
     */
    override fun toString(): String =
        "DateTime(value=$value, precision=$precision, zone=$zone)"

    private fun buildRegex(): Regex {
        val date = DATE_REGEX_SOURCE
        val timePart = when {
            // precision > 0: requires seconds and specific number of fractional digits
            precision != null && precision > 0 ->
                "([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d\\.\\d{$precision}"
            // precision == 0: seconds are optional, no fractional digits
            precision == 0 ->
                "([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?"
            // precision is null (default): seconds and fractional digits are optional
            else ->
                "([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d(\\.\\d+)?)?"
        }
        val timezonePart = when (zone) {
            Zone.LOCAL -> "" // No timezone for local
            Zone.OFFSET -> "(Z|[+-]\\d{2}:?\\d{2})" // 'Z' or offset
            Zone.UTC -> "Z" // Only 'Z'
        }
        val pattern = "^${date}T${timePart}$timezonePart$"
        return Regex(pattern)
    }

    public enum class Zone {
        /** Requires trailing 'Z'. */
        UTC,

        /** Accepts 'Z' or '±HH:MM' offset. */
        OFFSET,

        /** No timezone suffix (local time). */
        LOCAL,
    }

    public companion object {
        /**
         * Builds a [DateTime] that requires the trailing literal `Z` time-zone marker.
         *
         * @param value     the raw date-time string.
         * @param precision required number of fractional-second digits, or `null` to make them optional.
         */
        public fun utc(value: String, precision: Int? = null): DateTime =
            DateTime(value, precision, Zone.UTC)

        /**
         * Builds a [DateTime] that accepts either `Z` or a `±HH:MM` / `±HHMM` offset suffix.
         *
         * @param value     the raw date-time string.
         * @param precision required number of fractional-second digits, or `null` to make them optional.
         */
        public fun withOffset(value: String, precision: Int? = null): DateTime =
            DateTime(value, precision, Zone.OFFSET)

        /**
         * Builds a [DateTime] for local time — no zone suffix is permitted.
         *
         * @param value     the raw date-time string (must not include a time-zone suffix).
         * @param precision required number of fractional-second digits, or `null` to make them optional.
         */
        public fun local(value: String, precision: Int? = null): DateTime =
            DateTime(value, precision, Zone.LOCAL)
    }
}
