package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class Url(private val value: String) : ValueObject<String> {

    @Suppress("MaxLineLength")
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Url,
            rule = IoliteException.Rule.Format,
            condition = Regex(
                "^(https?)://(?!-)[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9](\\.[a-zA-Z]{2,})+(:[0-9]{1,5})?(/[a-zA-Z0-9\\-._~:/?#\\[\\]@!$&'()*+,;=]*)?$"
            ).matches(normalized) && normalized.length <= MAX_URL_LENGTH,
        ) {
            "Invalid URL: $value"
        }
        return normalized
    }

    companion object {
        private const val MAX_URL_LENGTH = 2048
    }
}
