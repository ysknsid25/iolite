package strings

import inorin.strings.DecimalString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class DecimalStringTest {

    @ParameterizedTest
    @MethodSource("validDecimalStrings")
    fun `parse should succeed for valid decimal strings`(input: String) {
        val decimalString = DecimalString(input)
        assertEquals(input, decimalString.parse().parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDecimalStrings")
    fun `parse should throw exception for invalid decimal strings`(input: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { DecimalString(input).parse() }
    }

    @ParameterizedTest
    @MethodSource("validDecimalStrings")
    fun `safeParse should return null for invalid decimal strings and value for valid ones`(input: String) {
        val result = DecimalString(input).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(input, result.getOrThrow().parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDecimalStrings")
    fun `safeParse should return failure for invalid inputs`(input: String) {
        val result = DecimalString(input).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validDecimalStrings(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("123.45"),
                Arguments.of("-123.45"),
                Arguments.of("0.0"),
                Arguments.of("+0.0")
            )
        }

        @JvmStatic
        fun invalidDecimalStrings(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("abc"),
                Arguments.of("123..45"),
                Arguments.of("12.34.56"),
                Arguments.of(""),
                Arguments.of(" ")
            )
        }
    }
}
