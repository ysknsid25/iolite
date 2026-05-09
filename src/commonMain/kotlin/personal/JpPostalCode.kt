package iolite.personal

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class JpPostalCode(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.JpPostalCode,
            rule = IoliteException.Rule.Format,
            condition = Regex("""^\d{3}-?\d{4}$""").matches(normalized),
        ) {
            "Invalid Japanese Postal Code: $value"
        }
        return normalized
    }
}
