package iolite.strings

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class IntegerString(private val value: String) : ValueObject<StringValueObject> {

    override fun parse(): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.IntegerString,
            rule = IoliteException.Rule.Format,
            condition = Regex("^[+-]?(0|[1-9][0-9]*)\$").matches(value),
        ) {
            "Invalid Integer String: $value"
        }
        return StringValueObject(value)
    }

    /**
     * Returns `IntegerString(value)`. The format is **not** part of the public
     * API contract and may change.
     */
    override fun toString(): String = "IntegerString($value)"
}
