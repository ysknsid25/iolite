package inorin.strings

import inorin.ValueObject

@JvmInline
value class AlphaNumericString(private val value: String) : ValueObject<StringValueObject> {

    override fun parse(): StringValueObject {
        require(Regex("^[a-zA-Z0-9]+$").matches(value)) {
            "Invalid Alphanumeric String: $value"
        }
        return StringValueObject(value)
    }
}
