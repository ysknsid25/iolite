package iolite.datetime

import iolite.IoliteException
import iolite.ValueObject
import iolite.datetime.Date.Companion.DATE_REGEX_SOURCE
import iolite.ioliteRequire

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
        public fun utc(value: String, precision: Int? = null): DateTime =
            DateTime(value, precision, Zone.UTC)

        public fun withOffset(value: String, precision: Int? = null): DateTime =
            DateTime(value, precision, Zone.OFFSET)

        public fun local(value: String, precision: Int? = null): DateTime =
            DateTime(value, precision, Zone.LOCAL)
    }
}
