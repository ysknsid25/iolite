package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Any hostname (including localhost and internal names)
 */
@JvmInline
value class HostName(private val value: String) : ValueObject<String> {

    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.HostName,
            rule = IoliteException.Rule.Format,
            condition = Regex("^([a-zA-Z0-9-]+\\.)*[a-zA-Z0-9-]+\$").matches(normalized),
        ) {
            "Invalid HostName: $value"
        }
        return normalized
    }
}
