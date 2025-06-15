package characters

import inorin.characters.IntegerCharacter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class IntegerCharacterTest {

    @ParameterizedTest
    @MethodSource("validIntegerInputs")
    fun `valid IntegerCharacter inputs should create successfully`(input: String) {
        val integerCharacter = IntegerCharacter(input)
        assertEquals(input, integerCharacter.parse())
    }

    @ParameterizedTest
    @MethodSource("invalidIntegerInputs")
    fun `invalid IntegerCharacter inputs should throw exceptions`(input: String) {
        assertThrows<IllegalArgumentException> { IntegerCharacter(input).parse() }
    }

    @ParameterizedTest
    @MethodSource("validIntegerInputs")
    fun `safeParse should return success for valid inputs`(input: String) {
        val result = IntegerCharacter(input).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(input, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidIntegerInputs")
    fun `safeParse should return failure for invalid inputs`(input: String) {
        val result = IntegerCharacter(input).safeParse()
        assertTrue(result.isFailure)
        assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validIntegerInputs() = listOf(
            "123",
            "0",
            "-456",
            "789",
            "-1"
        )

        @JvmStatic
        fun invalidIntegerInputs() = listOf(
            "abc",
            "12.34",
            "",
            " ",
            "1a",
            "--123",
            "++456",
            "123-",
            "123+"
        )
    }
}
