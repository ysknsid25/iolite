package inorin

@JvmInline
value class IntegerCharacter private constructor(private val value: String) : StringValueObject {
    override fun get(): String {
        return value
    }

    fun isPositive(): Boolean {
        return value.toIntOrNull()?.let { it > 0 } ?: false
    }

    fun isNegative(): Boolean {
        return value.toIntOrNull()?.let { it < 0 } ?: false
    }

    fun isZero(): Boolean {
        return value.toIntOrNull()?.let { it == 0 } ?: false
    }

    companion object {
        fun parse(input: String): IntegerCharacter {
            require(
                Regex("^[+-]?(0|[1-9][0-9]*)\$").matches(input)
            ) {
                "Invalid Integer Character: $input"
            }
            return IntegerCharacter(input)
        }

        fun safeParse(input: String): Result<IntegerCharacter> {
            return try {
                Result.success(parse(input))
            } catch (e: IllegalArgumentException) {
                Result.failure(e)
            }
        }
    }
}
