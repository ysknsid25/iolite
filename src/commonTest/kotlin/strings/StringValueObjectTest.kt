package strings

import iolite.IoliteException
import iolite.strings.StringValueObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringValueObjectTest {
    @Test
    fun parseShouldReturnTheCorrectValueForAValidString() {
        val valueObject = StringValueObject("validString")
        val result = valueObject.parse()
        assertEquals("validString", result)
    }

    @Test
    fun notEmptyShouldThrowExceptionForEmptyString() {
        val valueObject = StringValueObject("")
        val exception = assertFailsWith<IoliteException> {
            valueObject.notEmpty()
        }
        assertEquals("String value cannot be empty", exception.message)
        assertEquals(IoliteException.Target.StringValueObject, exception.target)
        assertEquals(IoliteException.Rule.NotEmpty, exception.rule)
    }

    @Test
    fun minLengthShouldThrowExceptionForStringShorterThanMinimumLength() {
        val valueObject = StringValueObject("short")
        val exception = assertFailsWith<IoliteException> {
            valueObject.min(10).parse()
        }
        assertEquals("Value short is less than minimum threshold 10", exception.message)
    }

    @Test
    fun maxLengthShouldThrowExceptionForStringLongerThanMaximumLength() {
        val valueObject = StringValueObject("this is a very long string")
        val exception = assertFailsWith<IoliteException> {
            valueObject.max(10).parse()
        }
        assertEquals("Value this is a very long string is greater than maximum threshold 10", exception.message)
    }

    @Test
    fun startWithShouldThrowExceptionForStringNotStartingWithSpecifiedPrefix() {
        val valueObject = StringValueObject("exampleString")
        val exception = assertFailsWith<IoliteException> {
            valueObject.startWith("test").parse()
        }
        assertEquals("Value exampleString does not start with test", exception.message)
    }

    @Test
    fun endWithShouldThrowExceptionForStringNotEndingWithSpecifiedSuffix() {
        val valueObject = StringValueObject("exampleString")
        val exception = assertFailsWith<IoliteException> {
            valueObject.endWith("test").parse()
        }
        assertEquals("Value exampleString does not end with test", exception.message)
    }

    @Test
    fun regexShouldThrowExceptionForStringNotMatchingRegexPattern() {
        val valueObject = StringValueObject("example123")
        val exception = assertFailsWith<IoliteException> {
            valueObject.regex(Regex("^[a-z]+$")).parse()
        }
        assertEquals("Value example123 does not match regex pattern ^[a-z]+$", exception.message)
    }

    @Test
    fun customerValidationShouldThrowExceptionForInvalidCustomValidation() {
        val valueObject = StringValueObject("invalid")
        val exception = assertFailsWith<IoliteException> {
            valueObject.customerValidation(
                validation = { it.length > 10 },
                errorMessage = "Custom validation failed"
            ).parse()
        }
        assertEquals("Custom validation failed", exception.message)
    }

    @Test
    fun methodChainingShouldSucceedAndReturnExpectedValue() {
        val valueObject = StringValueObject("prefix123suffix")
        val result = valueObject
            .notEmpty()
            .startWith("prefix")
            .endWith("suffix")
            .min(10)
            .max(20)
            .regex(Regex("^[a-zA-Z0-9]+$"))
            .customerValidation(
                validation = { it.contains("123") },
                errorMessage = "Custom validation failed"
            )
            .parse()

        assertEquals("prefix123suffix", result)
    }
}
