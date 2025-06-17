package strings

import iolite.strings.StringValueObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class StringValueObjectTest {

    @Test
    fun `parse should return the correct value for a valid string`() {
        val valueObject = StringValueObject("validString")
        val result = valueObject.parse()
        assertEquals("validString", result)
    }

    @Test
    fun `notEmpty should throw exception for empty string`() {
        val valueObject = StringValueObject("")
        val exception = assertThrows<IllegalArgumentException> {
            valueObject.notEmpty()
        }
        assertEquals("String value cannot be empty", exception.message)
    }

    @Test
    fun `min length should throw exception for string shorter than minimum length`() {
        val valueObject = StringValueObject("short")
        val exception = assertThrows<IllegalArgumentException> {
            valueObject.min(10).parse()
        }
        assertEquals("Value short is less than minimum threshold 10", exception.message)
    }

    @Test
    fun `max length should throw exception for string longer than maximum length`() {
        val valueObject = StringValueObject("this is a very long string")
        val exception = assertThrows<IllegalArgumentException> {
            valueObject.max(10).parse()
        }
        assertEquals("Value this is a very long string is greater than maximum threshold 10", exception.message)
    }

    @Test
    fun `startWith should throw exception for string not starting with specified prefix`() {
        val valueObject = StringValueObject("exampleString")
        val exception = assertThrows<IllegalArgumentException> {
            valueObject.startWith("test").parse()
        }
        assertEquals("Value exampleString does not start with test", exception.message)
    }

    @Test
    fun `endWith should throw exception for string not ending with specified suffix`() {
        val valueObject = StringValueObject("exampleString")
        val exception = assertThrows<IllegalArgumentException> {
            valueObject.endWith("test").parse()
        }
        assertEquals("Value exampleString does not end with test", exception.message)
    }

    @Test
    fun `regex should throw exception for string not matching regex pattern`() {
        val valueObject = StringValueObject("example123")
        val exception = assertThrows<IllegalArgumentException> {
            valueObject.regex("^[a-z]+$").parse()
        }
        assertEquals("Value example123 does not match regex pattern ^[a-z]+$", exception.message)
    }

    @Test
    fun `customerValidation should throw exception for invalid custom validation`() {
        val valueObject = StringValueObject("invalid")
        val exception = assertThrows<IllegalArgumentException> {
            valueObject.customerValidation(
                validation = { it.length > 10 },
                errorMessage = "Custom validation failed"
            ).parse()
        }
        assertEquals("Custom validation failed", exception.message)
    }

    @Test
    fun `method chaining should succeed and return expected value`() {
        val valueObject = StringValueObject("prefix123suffix")
        val result = valueObject
            .notEmpty()
            .startWith("prefix")
            .endWith("suffix")
            .min(10)
            .max(20)
            .regex("prefix\\d+suffix")
            .customerValidation(
                validation = { it.contains("123") },
                errorMessage = "Custom validation failed"
            )
            .parse()

        assertEquals("prefix123suffix", result)
    }
}
