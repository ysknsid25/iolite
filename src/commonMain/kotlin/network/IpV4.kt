package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class IpV4(private val value: String) : ValueObject<String> {

    @Suppress("MaxLineLength")
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.IpV4,
            rule = IoliteException.Rule.Format,
            condition = ipv4Regex.matches(normalized),
        ) {
            "Invalid IPV4 Address: $value"
        }
        return normalized
    }

    /**
     * Returns `IpV4(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "IpV4($value)"

    companion object {
        @Suppress("MaxLineLength")
        private val ipv4Regex = Regex(
            "^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\$"
        )
    }
}
