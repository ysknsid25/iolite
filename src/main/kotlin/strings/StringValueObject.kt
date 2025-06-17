package iolite.strings

import iolite.ValueObject

@JvmInline
value class StringValueObject(private val value: String) : ValueObject<String> {

    override fun parse(): String {
        return value
    }

    fun notEmpty(): StringValueObject {
        require(value.isNotEmpty()) {
            "String value cannot be empty"
        }
        return this
    }

    fun min(threshold: Int): StringValueObject {
        require(value.length >= threshold) {
            "Value $value is less than minimum threshold $threshold"
        }
        return this
    }

    fun max(threshold: Int): StringValueObject {
        require(value.length <= threshold) {
            "Value $value is greater than maximum threshold $threshold"
        }
        return this
    }

    fun startWith(prefix: String): StringValueObject {
        require(value.startsWith(prefix)) {
            "Value $value does not start with $prefix"
        }
        return this
    }

    fun endWith(suffix: String): StringValueObject {
        require(value.endsWith(suffix)) {
            "Value $value does not end with $suffix"
        }
        return this
    }

    fun regex(pattern: String): StringValueObject {
        require(value.matches(Regex(pattern))) {
            "Value $value does not match regex pattern $pattern"
        }
        return this
    }

    fun customerValidation(validation: (String) -> Boolean, errorMessage: String): StringValueObject {
        require(validation(value)) {
            errorMessage
        }
        return this
    }
}
