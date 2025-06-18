package personal

import iolite.personal.CreditCardNumber
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CreditCardNumberTest {
    @ParameterizedTest
    @MethodSource("validCardNumbers")
    fun `valid URLs should parse successfully`(cardNumber: String) {
        assertEquals(cardNumber, CreditCardNumber(cardNumber).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidCardNumbers")
    fun `invalid URLs should throw exceptions`(cardNumber: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { CreditCardNumber(cardNumber).parse() }
    }

    @ParameterizedTest
    @MethodSource("validCardNumbers")
    fun `safeParse should return success for valid inputs`(cardNumber: String) {
        val result = CreditCardNumber(cardNumber).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(cardNumber, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidCardNumbers")
    fun `safeParse should return failure for invalid inputs`(cardNumber: String) {
        val result = CreditCardNumber(cardNumber).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validCardNumbers() = listOf(
            // Amex
            "378282246310005",
            "371449635398431",
            // Diners
            "3056930009020004",
            "36227206271667",
            // Discover
            "6011111111111117",
            "6011000990139424",
            "6011981111111113",
            // JCB
            "3530111333300000",
            "3566002020360505",
            // Master
            "6200000000000005",
            "6200000000000047",
            "6205500000000000004",
            // Visa
            "4242424242424242",
            "4000056655665556",
            // with space dividers
            "4000 0025 0000 1001",
            "5555 5525 0000 1001",
            // with hyphen dividers
            "4000-0503-6000-0001",
            "5555-0503-6000-0080",
        )

        @Suppress("MaxLineLength")
        @JvmStatic
        fun invalidCardNumbers() = listOf(
            "",
            " ",
            "\n",
            "DE6200000000000005",
            "6011000A90139424",
            "37828224631000E",
            "40000025 0000-1001",
            "5555-55250000 1001",
            "5555 55555555-4444",
            "4000  0025 0000 1001",
            "5555-0503--6000-0080",
            "3782822463100",
            "3622720627166",
            "62055000000000000040",
            "7530111333300000",
            "1105105105105100",
            "9000056655665556",
            "5200828282828211",
            "371449635398434"
        )
    }
}
