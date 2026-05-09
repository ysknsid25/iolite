package strings

import iolite.IoliteException
import iolite.strings.AlphaNumericString
import iolite.strings.StringValueObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AlphaNumericStringTest {
    @Test
    fun validAlphaNumericStringInputsShouldParseSuccessfully() {
        for (input in validAlphaNumericInputs) {
            val result = AlphaNumericString(input).parse()
            assertEquals(input, result.parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidAlphaNumericStringInputsShouldThrowExceptionsOnParse() {
        for (input in invalidAlphaNumericInputs) {
            assertFailsWith<IoliteException>("Expected fail for input='$input'") {
                AlphaNumericString(input).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidAlphaNumericStringInputs() {
        for (input in validAlphaNumericInputs) {
            val result: Result<StringValueObject> = runCatching { AlphaNumericString(input).parse() }
            assertTrue(result.isSuccess, "Expected success for input='$input'")
            assertEquals(input, result.getOrThrow().parse())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidAlphaNumericStringInputs() {
        for (input in invalidAlphaNumericInputs) {
            val result: Result<StringValueObject> = runCatching { AlphaNumericString(input).parse() }
            assertTrue(result.isFailure, "Expected failure for input='$input'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    companion object {
        private val validAlphaNumericInputs = listOf(
            "abc123",
            "ABC",
            "123",
            "a1b2c3",
            "A1B2C3",
        )

        private val invalidAlphaNumericInputs = listOf(
            "abc 123",
            "abc@123",
            "123!",
            "abc#",
            "abc_123",
            "",
        )
    }
}
