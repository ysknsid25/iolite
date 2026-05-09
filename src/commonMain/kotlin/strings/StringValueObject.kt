package iolite.strings

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

@JvmInline
value class StringValueObject(private val value: String) : ValueObject<String> {

    override fun parse(): String {
        return value
    }

    fun notEmpty(): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.NotEmpty,
            condition = value.isNotEmpty(),
        ) {
            "String value cannot be empty"
        }
        return this
    }

    fun min(threshold: Int): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.Min,
            condition = value.length >= threshold,
        ) {
            "Value $value is less than minimum threshold $threshold"
        }
        return this
    }

    fun max(threshold: Int): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.Max,
            condition = value.length <= threshold,
        ) {
            "Value $value is greater than maximum threshold $threshold"
        }
        return this
    }

    fun startWith(prefix: String): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.StartWith,
            condition = value.startsWith(prefix),
        ) {
            "Value $value does not start with $prefix"
        }
        return this
    }

    fun endWith(suffix: String): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.EndWith,
            condition = value.endsWith(suffix),
        ) {
            "Value $value does not end with $suffix"
        }
        return this
    }

    fun regex(regex: Regex): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.Regex,
            condition = value.matches(regex),
        ) {
            "Value $value does not match regex pattern ${regex.pattern}"
        }
        return this
    }

    fun customerValidation(validation: (String) -> Boolean, errorMessage: String): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.CustomerValidation,
            condition = validation(value),
        ) {
            errorMessage
        }
        return this
    }
}
