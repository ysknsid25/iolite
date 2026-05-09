package strings

import iolite.strings.DecimalString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DecimalStringTest {
    @Test
    fun parseShouldSucceedForValidDecimalStrings() {
        for (input in validDecimalStrings) {
            assertEquals(input, DecimalString(input).parse().parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun parseShouldThrowExceptionForInvalidDecimalStrings() {
        for (input in invalidDecimalStrings) {
            assertFailsWith<IllegalArgumentException>("Expected fail for input='$input'") {
                DecimalString(input).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnValueForValidDecimalStrings() {
        for (input in validDecimalStrings) {
            val result = DecimalString(input).safeParse()
            assertTrue(result.isSuccess, "Expected success for input='$input'")
            assertEquals(input, result.getOrThrow().parse())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (input in invalidDecimalStrings) {
            val result = DecimalString(input).safeParse()
            assertTrue(result.isFailure, "Expected failure for input='$input'")
            assertFailsWith<IllegalArgumentException> { result.getOrThrow() }
        }
    }

    companion object {
        private val validDecimalStrings = listOf(
            "123.45",
            "-123.45",
            "0.0",
            "+0.0",
        )

        private val invalidDecimalStrings = listOf(
            "abc",
            "123..45",
            "12.34.56",
            "",
            " ",
        )
    }
}
