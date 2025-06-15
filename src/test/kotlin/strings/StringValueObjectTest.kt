package strings

import inorin.strings.StringValueObject
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
    fun `method chaining should succeed and return expected value`() {
        val valueObject = StringValueObject("prefix123suffix")
        val result = valueObject
            .notEmpty()
            .startWith("prefix")
            .endWith("suffix")
            .min(10)
            .max(20)
            .parse()

        assertEquals("prefix123suffix", result)
    }
}
