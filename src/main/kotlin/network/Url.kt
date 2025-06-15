package inorin.network

import inorin.ValueObject

@JvmInline
value class Url(private val value: String) : ValueObject {

    @Suppress("MaxLineLength")
    override fun parse(): String {
        val normalized = value.trim()
        require(
            Regex(
                "^(https?)://(?!-)[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9](\\.[a-zA-Z]{2,})+(:[0-9]{1,5})?(/[a-zA-Z0-9\\-._~:/?#\\[\\]@!$&'()*+,;=]*)?$"
            ).matches(normalized) && normalized.length <= MAX_URL_LENGTH
        ) {
            "Invalid URL: $value"
        }
        return value
    }

    companion object {
        private const val MAX_URL_LENGTH = 2048
    }
}
