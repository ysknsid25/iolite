package personal

import iolite.personal.JpPhoneNumber
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class JpPhoneNumberTest {
    @ParameterizedTest
    @MethodSource("validJpPhoneNumber")
    fun `valid Japanese Phone Number should parse successfully`(jpPhoneNumber: String) {
        assertEquals(jpPhoneNumber, JpPhoneNumber(jpPhoneNumber).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidJpPhoneNumber")
    fun `invalid Japanese Phone Number should throw exceptions`(jpPhoneNumber: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { JpPhoneNumber(jpPhoneNumber).parse() }
    }

    @ParameterizedTest
    @MethodSource("validJpPhoneNumber")
    fun `safeParse should return success for valid inputs`(jpPhoneNumber: String) {
        val result = JpPhoneNumber(jpPhoneNumber).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(jpPhoneNumber, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidJpPhoneNumber")
    fun `safeParse should return failure for invalid inputs`(jpPhoneNumber: String) {
        val result = JpPhoneNumber(jpPhoneNumber).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validJpPhoneNumber() = listOf(
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
            "0570064000"
        )

        @JvmStatic
        fun invalidJpPhoneNumber() = listOf(
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
            "057-0064-000"
        )
    }
}
