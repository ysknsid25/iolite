package iolite.personal

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class Email(private val value: String) : ValueObject<String> {

    @Suppress("MaxLineLength")
    override fun parse(): String {
        val normalized = value.trim().lowercase()
        ioliteRequire(
            target = IoliteException.Target.Email,
            rule = IoliteException.Rule.Format,
            condition = Regex(
                "^(?!\\.)(?!.*\\.\\.)([A-Za-z0-9_'+\\-\\.]*)[A-Za-z0-9_+\\-]@([A-Za-z0-9][A-Za-z0-9\\-]*\\.)+[A-Za-z]{2,}$"
            ).matches(normalized)
        ) {
            "Invalid email address"
        }
        return normalized
    }

    /**
     * Returns a masked representation: e.g. `Email(j***@example.com)`.
     * The local-part is masked except for its first character so that PII
     * does not leak into logs / debugger output. The exact mask format is
     * **not** part of the public API contract and may change.
     */
    override fun toString(): String {
        val at = value.indexOf('@')
        if (at <= 0) return "Email(***)"
        val local = value.substring(0, at)
        val domain = value.substring(at)
        val masked = local.first() + "***"
        return "Email($masked$domain)"
    }
}
