package iolite.strings

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class DecimalString(private val value: String) : ValueObject<StringValueObject> {

    override fun parse(): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.DecimalString,
            rule = IoliteException.Rule.Format,
            condition = Regex("^[+-]?([0-9]+\\.[0-9]+|[0-9]+)\$").matches(value),
        ) {
            "Invalid Decimal String: $value"
        }
        return StringValueObject(value)
    }

    /**
     * Returns `DecimalString(value)`. The format is **not** part of the public
     * API contract and may change.
     */
    override fun toString(): String = "DecimalString($value)"
}
