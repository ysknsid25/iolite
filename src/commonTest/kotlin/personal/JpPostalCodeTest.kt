package personal

import iolite.personal.JpPostalCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class JpPostalCodeTest {
    @ParameterizedTest
    @MethodSource("validJpPostalCodes")
    fun `valid Japanese Postal Code should parse successfully`(jpPostalCode: String) {
        assertEquals(jpPostalCode, JpPostalCode(jpPostalCode).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidJpPostalCodes")
    fun `invalid Japanese Postal Code should throw exceptions`(jpPostalCode: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { JpPostalCode(jpPostalCode).parse() }
    }

    @ParameterizedTest
    @MethodSource("validJpPostalCodes")
    fun `safeParse should return success for valid inputs`(jpPostalCode: String) {
        val result = JpPostalCode(jpPostalCode).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(jpPostalCode, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidJpPostalCodes")
    fun `safeParse should return failure for invalid inputs`(jpPostalCode: String) {
        val result = JpPostalCode(jpPostalCode).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validJpPostalCodes() = listOf(
            "123-4567",
            "001-0001",
            "999-9999",
            "1234567",
            "0000000"
        )

        @JvmStatic
        fun invalidJpPostalCodes() = listOf(
            "12-34567",
            "1234-567",
            "1234-5678",
            "123_4567",
            "１２３−４５６７",
            "123456",
            "12345678",
            "abc-defg",
            "123-45a7",
            "",
            " ",
            "\n",
            "１２３ー４５６７"
        )
    }
}
