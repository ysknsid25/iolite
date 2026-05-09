package iolite.strings

import iolite.ValueObject
import kotlin.jvm.JvmInline

@JvmInline
value class AlphaNumericString(private val value: String) : ValueObject<StringValueObject> {

    override fun parse(): StringValueObject {
        require(Regex("^[a-zA-Z0-9]+$").matches(value)) {
            "Invalid Alphanumeric String: $value"
        }
        return StringValueObject(value)
    }
}
