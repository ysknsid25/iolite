package personal

import iolite.IoliteException
import iolite.personal.JpPostalCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class JpPostalCodeTest {
    @Test
    fun validJapanesePostalCodeShouldParseSuccessfully() {
        for (jpPostalCode in validJpPostalCodes) {
            assertEquals(
                jpPostalCode,
                JpPostalCode(jpPostalCode).parse(),
                "Failed for jpPostalCode='$jpPostalCode'"
            )
        }
    }

    @Test
    fun invalidJapanesePostalCodeShouldThrowExceptions() {
        for (jpPostalCode in invalidJpPostalCodes) {
            assertFailsWith<IoliteException>("Expected fail for jpPostalCode='$jpPostalCode'") {
                JpPostalCode(jpPostalCode).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (jpPostalCode in validJpPostalCodes) {
            val result = JpPostalCode(jpPostalCode).safeParse()
            assertTrue(result.isSuccess, "Expected success for jpPostalCode='$jpPostalCode'")
            assertEquals(jpPostalCode, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (jpPostalCode in invalidJpPostalCodes) {
            val result = JpPostalCode(jpPostalCode).safeParse()
            assertTrue(result.isFailure, "Expected failure for jpPostalCode='$jpPostalCode'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    @Test
    fun toStringShouldRenderClassNameAndValue() {
        assertEquals("JpPostalCode(123-4567)", JpPostalCode("123-4567").toString())
    }

    companion object {
        private val validJpPostalCodes = listOf(
            "123-4567",
            "001-0001",
            "999-9999",
            "1234567",
            "0000000",
        )

        private val invalidJpPostalCodes = listOf(
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
            "１２３ー４５６７",
        )
    }
}
