package personal

import iolite.personal.CreditCardNumber
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CreditCardNumberTest {
    @Test
    fun validCardNumbersShouldParseSuccessfully() {
        for (cardNumber in validCardNumbers) {
            assertEquals(cardNumber, CreditCardNumber(cardNumber).parse(), "Failed for cardNumber='$cardNumber'")
        }
    }

    @Test
    fun invalidCardNumbersShouldThrowExceptions() {
        for (cardNumber in invalidCardNumbers) {
            assertFailsWith<IllegalArgumentException>("Expected fail for cardNumber='$cardNumber'") {
                CreditCardNumber(cardNumber).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (cardNumber in validCardNumbers) {
            val result = CreditCardNumber(cardNumber).safeParse()
            assertTrue(result.isSuccess, "Expected success for cardNumber='$cardNumber'")
            assertEquals(cardNumber, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (cardNumber in invalidCardNumbers) {
            val result = CreditCardNumber(cardNumber).safeParse()
            assertTrue(result.isFailure, "Expected failure for cardNumber='$cardNumber'")
            assertFailsWith<IllegalArgumentException> { result.getOrThrow() }
        }
    }

    companion object {
        private val validCardNumbers = listOf(
            "378282246310005",
            "371449635398431",
            "3056930009020004",
            "36227206271667",
            "6011111111111117",
            "6011000990139424",
            "6011981111111113",
            "3530111333300000",
            "3566002020360505",
            "6200000000000005",
            "6200000000000047",
            "6205500000000000004",
            "4242424242424242",
            "4000056655665556",
            "4000 0025 0000 1001",
            "5555 5525 0000 1001",
            "4000-0503-6000-0001",
            "5555-0503-6000-0080",
        )

        @Suppress("MaxLineLength")
        private val invalidCardNumbers = listOf(
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
            "371449635398434",
        )
    }
}
