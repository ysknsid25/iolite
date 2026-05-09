package iolite.strings

import iolite.ValueObject
import kotlin.jvm.JvmInline

@JvmInline
value class IntegerString(private val value: String) : ValueObject<StringValueObject> {

    override fun parse(): StringValueObject {
        require(
            Regex("^[+-]?(0|[1-9][0-9]*)\$").matches(value)
        ) {
            "Invalid Integer String: $value"
        }
        return StringValueObject(value)
    }
}
