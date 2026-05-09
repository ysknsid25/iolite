package iolite.encoding

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class Base64(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Base64,
            rule = IoliteException.Rule.Format,
            condition = Regex(
                "^(?:[\\da-z+/]{4})*(?:[\\da-z+/]{2}==|[\\da-z+/]{3}=)?$",
                setOf(RegexOption.IGNORE_CASE)
            ).matches(normalized),
        ) {
            "Invalid Base64 string: $value"
        }
        return normalized
    }

    /**
     * Returns `Base64(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "Base64($value)"
}
