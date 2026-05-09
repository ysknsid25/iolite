package personal

import iolite.IoliteException
import iolite.personal.JpPhoneNumber
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class JpPhoneNumberTest {
    @Test
    fun validJapanesePhoneNumberShouldParseSuccessfully() {
        for (jpPhoneNumber in validJpPhoneNumber) {
            assertEquals(
                jpPhoneNumber,
                JpPhoneNumber(jpPhoneNumber).parse(),
                "Failed for jpPhoneNumber='$jpPhoneNumber'"
            )
        }
    }

    @Test
    fun invalidJapanesePhoneNumberShouldThrowExceptions() {
        for (jpPhoneNumber in invalidJpPhoneNumber) {
            assertFailsWith<IoliteException>("Expected fail for jpPhoneNumber='$jpPhoneNumber'") {
                JpPhoneNumber(jpPhoneNumber).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (jpPhoneNumber in validJpPhoneNumber) {
            val result = JpPhoneNumber(jpPhoneNumber).safeParse()
            assertTrue(result.isSuccess, "Expected success for jpPhoneNumber='$jpPhoneNumber'")
            assertEquals(jpPhoneNumber, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (jpPhoneNumber in invalidJpPhoneNumber) {
            val result = JpPhoneNumber(jpPhoneNumber).safeParse()
            assertTrue(result.isFailure, "Expected failure for jpPhoneNumber='$jpPhoneNumber'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    @Test
    fun toStringShouldMaskAllButLast4Digits() {
        assertEquals("JpPhoneNumber(***-****-5678)", JpPhoneNumber("03-1234-5678").toString())
        assertEquals("JpPhoneNumber(***-****-5678)", JpPhoneNumber("09012345678").toString())
    }

    @Test
    fun toStringMustNotExposeFullPhoneNumber() {
        val rendered = JpPhoneNumber("03-1234-5678").toString()
        assertTrue(
            !rendered.contains("1234"),
            "toString must not expose digits before the last 4: $rendered"
        )
    }

    companion object {
        private val validJpPhoneNumber = listOf(
            "03-1234-5678",
            "011-123-4567",
            "09969-1-2345",
            "090-1234-5678",
            "050-1234-5678",
            "0120-123-456",
            "0800-123-4567",
            "0570-064-000",
            "0312345678",
            "0111234567",
            "0996912345",
            "09012345678",
            "05012345678",
            "0120123456",
            "08012345678",
            "0570064000",
        )

        private val invalidJpPhoneNumber = listOf(
            "123-456-7890",
            "080-123-45678",
            "03--1234-5678",
            "031234567",
            "0900-1234-5678",
            "0800-1234-56",
            "abcd-efgh-ijkl",
            "050_1234_5678",
            "0570-06-4000",
            "0570-0640-00",
            "057-0064-000",
        )
    }
}
