package strings

import iolite.strings.IntegerString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IntegerStringTest {
    @Test
    fun `valid IntegerCharacter inputs should create successfully`() {
        for (input in validIntegerInputs) {
            val integerString = IntegerString(input)
            assertEquals(input, integerString.parse().parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun `invalid IntegerCharacter inputs should throw exceptions`() {
        for (input in invalidIntegerInputs) {
            assertFailsWith<IllegalArgumentException>("Expected fail for input='$input'") {
                IntegerString(input).parse()
            }
        }
    }

    @Test
    fun `safeParse should return success for valid inputs`() {
        for (input in validIntegerInputs) {
            val result = IntegerString(input).safeParse()
            assertTrue(result.isSuccess, "Expected success for input='$input'")
            assertEquals(input, result.getOrThrow().parse())
        }
    }

    @Test
    fun `safeParse should return failure for invalid inputs`() {
        for (input in invalidIntegerInputs) {
            val result = IntegerString(input).safeParse()
            assertTrue(result.isFailure, "Expected failure for input='$input'")
            assertFailsWith<IllegalArgumentException> { result.getOrThrow() }
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
