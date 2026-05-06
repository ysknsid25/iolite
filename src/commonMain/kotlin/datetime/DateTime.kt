package iolite.datetime

import iolite.ValueObject
import iolite.datetime.Date.Companion.DATE_REGEX_SOURCE

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
        require(
            regex.matches(normalized)
        ) { "Invalid datetime format: '$value' for precision: $precision, offset: $offset, local: $local" }
        return normalized
    }

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
