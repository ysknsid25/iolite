package iolite.datetime

import iolite.IoliteException
import iolite.ValueObject
import iolite.datetime.Date.Companion.DATE_REGEX_SOURCE
import iolite.ioliteRequire

@Suppress("ArgumentListWrapping")
class DateTime(
    private val value: String,
    private val precision: Int? = null,
    private val offset: Boolean = false,
    private val local: Boolean = false,
) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        val regex = buildRegex()
        ioliteRequire(
            target = IoliteException.Target.DateTime,
            rule = IoliteException.Rule.Format,
            condition = regex.matches(normalized),
        ) {
            "Invalid datetime format: '$value' for precision: $precision, offset: $offset, local: $local"
        }
        return normalized
    }

    /**
     * Returns `DateTime(value=…, precision=…, offset=…, local=…)`. The format
     * is **not** part of the public API contract and may change.
     */
    override fun toString(): String =
        "DateTime(value=$value, precision=$precision, offset=$offset, local=$local)"

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
        val timezonePart = when {
            local -> "" // No timezone for local
            offset -> "(Z|[+-]\\d{2}:?\\d{2})" // 'Z' or offset
            else -> "Z" // Only 'Z'
        }
        val pattern = "^${date}T${timePart}$timezonePart$"
        return Regex(pattern)
    }
}
