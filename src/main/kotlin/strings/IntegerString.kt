package inorin.characters

import inorin.ValueObject

@JvmInline
value class IntegerString(private val value: String) : ValueObject {

    override fun parse(): String {
        require(
            Regex("^[+-]?(0|[1-9][0-9]*)\$").matches(value)
        ) {
            "Invalid Integer String: $value"
        }
        return value
    }
}
