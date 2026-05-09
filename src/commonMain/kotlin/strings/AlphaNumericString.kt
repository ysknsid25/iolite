package iolite.strings

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class AlphaNumericString(private val value: String) : ValueObject<StringValueObject> {

    override fun parse(): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.AlphaNumericString,
            rule = IoliteException.Rule.Format,
            condition = Regex("^[a-zA-Z0-9]+$").matches(value),
        ) {
            "Invalid Alphanumeric String: $value"
        }
        return StringValueObject(value)
    }

    /**
     * Returns `AlphaNumericString(value)`. The format is **not** part of the
     * public API contract and may change.
     */
    override fun toString(): String = "AlphaNumericString($value)"
}
