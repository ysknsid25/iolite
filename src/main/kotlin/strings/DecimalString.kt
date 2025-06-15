package iolite.strings

import iolite.ValueObject

@JvmInline
value class DecimalString(private val value: String) : ValueObject<StringValueObject> {

    override fun parse(): StringValueObject {
        require(
            Regex("^[+-]?([0-9]+\\.[0-9]+|[0-9]+)\$").matches(value)
        ) {
            "Invalid Decimal String: $value"
        }
        return StringValueObject(value)
    }
}
