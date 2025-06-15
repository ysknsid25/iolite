import inorin.IntegerCharacter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class IntegerCharacterTest {

    @ParameterizedTest
    @MethodSource("validIntegerInputs")
    fun `valid IntegerCharacter inputs should create successfully`(input: String) {
        val integerCharacter = IntegerCharacter.parse(input)
        assertEquals(input, integerCharacter.get())
    }

    @ParameterizedTest
    @MethodSource("invalidIntegerInputs")
    fun `invalid IntegerCharacter inputs should throw exceptions`(input: String) {
        assertThrows<IllegalArgumentException> { IntegerCharacter.parse(input) }
    }

    @Test
    fun `toInt should return correct integer value`() {
        val integerCharacter = IntegerCharacter.parse("123")
        assertEquals(123, integerCharacter.toInt())
    }

    @Test
    fun `isPositive should return true for positive integers`() {
        val positiveInteger = IntegerCharacter.parse("123")
        assertTrue(positiveInteger.isPositive())
    }

    @Test
    fun `isPositive should return false for non-positive integers`() {
        val zeroInteger = IntegerCharacter.parse("0")
        val negativeInteger = IntegerCharacter.parse("-123")
        assertFalse(zeroInteger.isPositive())
        assertFalse(negativeInteger.isPositive())
    }

    @Test
    fun `isZero should return true for zero`() {
        val zeroInteger = IntegerCharacter.parse("0")
        assertTrue(zeroInteger.isZero())
    }

    @Test
    fun `isZero should return false for non-zero integers`() {
        val positiveInteger = IntegerCharacter.parse("123")
        val negativeInteger = IntegerCharacter.parse("-123")
        assertFalse(positiveInteger.isZero())
        assertFalse(negativeInteger.isZero())
    }

    @Test
    fun `isNegative should return true for negative integers`() {
        val negativeInteger = IntegerCharacter.parse("-123")
        assertTrue(negativeInteger.isNegative())
    }

    @Test
    fun `isNegative should return false for non-negative integers`() {
        val positiveInteger = IntegerCharacter.parse("123")
        val zeroInteger = IntegerCharacter.parse("0")
        assertFalse(positiveInteger.isNegative())
        assertFalse(zeroInteger.isNegative())
    }

    @ParameterizedTest
    @MethodSource("validIntegerInputs")
    fun `safeParse should return success for valid inputs`(input: String) {
        val result = IntegerCharacter.safeParse(input)
        assertTrue(result.isSuccess)
        assertEquals(input, result.getOrThrow().get())
    }

    @ParameterizedTest
    @MethodSource("invalidIntegerInputs")
    fun `safeParse should return failure for invalid inputs`(input: String) {
        val result = IntegerCharacter.safeParse(input)
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
