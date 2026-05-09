package strings

import iolite.IoliteException
import iolite.strings.IntegerString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IntegerStringTest {
    @Test
    fun validIntegerCharacterInputsShouldCreateSuccessfully() {
        for (input in validIntegerInputs) {
            val integerString = IntegerString(input)
            assertEquals(input, integerString.parse().parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidIntegerCharacterInputsShouldThrowExceptions() {
        for (input in invalidIntegerInputs) {
            assertFailsWith<IoliteException>("Expected fail for input='$input'") {
                IntegerString(input).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (input in validIntegerInputs) {
            val result = IntegerString(input).safeParse()
            assertTrue(result.isSuccess, "Expected success for input='$input'")
            assertEquals(input, result.getOrThrow().parse())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (input in invalidIntegerInputs) {
            val result = IntegerString(input).safeParse()
            assertTrue(result.isFailure, "Expected failure for input='$input'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    companion object {
        private val validIntegerInputs = listOf(
            "123",
            "0",
            "-456",
            "789",
            "-1",
        )

        private val invalidIntegerInputs = listOf(
            "abc",
            "12.34",
            "",
            " ",
            "1a",
            "--123",
            "++456",
            "123-",
            "123+",
        )
    }
}
