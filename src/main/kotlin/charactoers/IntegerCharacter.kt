package inorin.charactoers

import inorin.ValueObject

@JvmInline
value class IntegerCharacter(private val value: String) : ValueObject {

    override fun parse(): String {
        require(
            Regex("^[+-]?(0|[1-9][0-9]*)\$").matches(value)
        ) {
            "Invalid Integer Character: $value"
        }
        return value
    }
}
