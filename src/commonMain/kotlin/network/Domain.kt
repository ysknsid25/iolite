package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Exact Domain Name (for FQDN)
 */
@JvmInline
value class Domain(private val value: String) : ValueObject<String> {

    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Domain,
            rule = IoliteException.Rule.Format,
            condition = Regex("^([a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}\$").matches(normalized),
        ) {
            "Invalid Domain: $value"
        }
        return normalized
    }
}
