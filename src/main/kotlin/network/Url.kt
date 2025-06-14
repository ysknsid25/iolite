package inorin.network

import inorin.StringValueObject

@JvmInline
value class Url private constructor(private val value: String) : StringValueObject {

    override fun get(): String {
        return value
    }

    companion object {

        private const val MAX_URL_LENGTH = 2048

        @Suppress("MaxLineLength")
        fun of(input: String): Url {
            val normalized = input.trim()
            require(
                Regex(
                    "^(https?)://(?!localhost)(?!-)[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9](\\.[a-zA-Z]{2,})+(:[0-9]{1,5})?(/[a-zA-Z0-9\\-._~:/?#\\[\\]@!$&'()*+,;=]*)?$"
                ).matches(normalized) && normalized.length <= MAX_URL_LENGTH
            ) {
                "Invalid URL: $input"
            }
            return Url(normalized)
        }
    }
}
