package inorin.personal

import inorin.ValueObject

@JvmInline
value class Email(private val value: String) : ValueObject<String> {

    @Suppress("MaxLineLength")
    override fun parse(): String {
        val normalized = value.trim().lowercase()
        require(
            Regex(
                "^(?!\\.)(?!.*\\.\\.)([A-Za-z0-9_'+\\-\\.]*)[A-Za-z0-9_+\\-]@([A-Za-z0-9][A-Za-z0-9\\-]*\\.)+[A-Za-z]{2,}$"
            ).matches(normalized)
        ) {
            "Invalid email address: $value"
        }
        return normalized
    }
}
