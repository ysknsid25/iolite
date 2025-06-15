package inorin.personal

import inorin.StringValueObject

@JvmInline
value class Email private constructor(private val value: String) : StringValueObject {

    override fun get(): String {
        return value
    }

    companion object {
        @Suppress("MaxLineLength")
        fun parse(input: String): Email {
            val normalized = input.trim().lowercase()
            require(
                Regex(
                    "^(?!\\.)(?!.*\\.\\.)([A-Za-z0-9_'+\\-\\.]*)[A-Za-z0-9_+\\-]@([A-Za-z0-9][A-Za-z0-9\\-]*\\.)+[A-Za-z]{2,}$"
                ).matches(normalized)
            ) {
                "Invalid email address: $input"
            }
            return Email(normalized)
        }

        fun safeParse(input: String): Result<Email> {
            return try {
                Result.success(parse(input))
            } catch (e: IllegalArgumentException) {
                Result.failure(e)
            }
        }
    }
}
