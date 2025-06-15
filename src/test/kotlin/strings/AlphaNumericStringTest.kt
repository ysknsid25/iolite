package strings

import inorin.strings.AlphaNumericString
import inorin.strings.StringValueObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.Result

class AlphaNumericStringTest {

    @ParameterizedTest
    @MethodSource("validAlphaNumericInputs")
    fun `valid AlphaNumericString inputs should parse successfully`(input: String) {
        val alphaNumericString = AlphaNumericString(input)
        val result = alphaNumericString.parse()
        assertEquals(input, result.parse())
    }

    @ParameterizedTest
    @MethodSource("invalidAlphaNumericInputs")
    fun `invalid AlphaNumericString inputs should throw exceptions on parse`(input: String) {
        assertThrows<IllegalArgumentException> {
            AlphaNumericString(input).parse()
        }
    }

    @ParameterizedTest
    @MethodSource("validAlphaNumericInputs")
    fun `safeParse should return success for valid AlphaNumericString inputs`(input: String) {
        val result: Result<StringValueObject> = runCatching { AlphaNumericString(input).parse() }
        assertTrue(result.isSuccess)
        assertEquals(input, result.getOrThrow().parse())
    }

    @ParameterizedTest
    @MethodSource("invalidAlphaNumericInputs")
    fun `safeParse should return failure for invalid AlphaNumericString inputs`(input: String) {
        val result: Result<StringValueObject> = runCatching { AlphaNumericString(input).parse() }
        assertTrue(result.isFailure)
        assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validAlphaNumericInputs() = listOf(
            "abc123",
            "ABC",
            "123",
            "a1b2c3",
            "A1B2C3"
        )

        @JvmStatic
        fun invalidAlphaNumericInputs() = listOf(
            "abc 123", // Contains space
            "abc@123", // Contains special character
            "123!", // Contains special character
            "abc#", // Contains special character
            "abc_123", // Contains underscore
            "" // Empty string
        )
    }
}
