package iolite.personal

import iolite.ValueObject

@JvmInline
value class CreditCardNumber(private val value: String) : ValueObject<String> {

    override fun parse(): String {
        require(CREDIT_CARD_REGEX.matches(value)) {
            "Invalid credit card format: $value"
        }
        val sanitizedNumber = value.replace(SANITIZE_REGEX, "")
        require(PROVIDER_REGEX_LIST.any { it.matches(sanitizedNumber) }) {
            "Unknown card provider: $value"
        }
        require(isLuhnAlgo(sanitizedNumber)) {
            "Invalid credit card number (Luhn check failed): $value"
        }
        return value
    }

    @Suppress("MagicNumber")
    private fun isLuhnAlgo(input: String): Boolean {
        val number = input.replace(NON_DIGIT_REGEX, "")

        var length = number.length
        var bit = 1
        var sum = 0

        while (length > 0) {
            val value = number[--length].digitToInt()
            bit = bit xor 1
            sum += if (bit == 1) {
                arrayOf(0, 2, 4, 6, 8, 1, 3, 5, 7, 9)[value]
            } else {
                value
            }
        }

        return sum % 10 == 0
    }

    companion object {
        private val SANITIZE_REGEX = Regex("[- ]")
        private val NON_DIGIT_REGEX = Regex("\\D")
        private val CREDIT_CARD_REGEX = Regex(
            """^(?:\d{14,19}|\d{4}(?: \d{3,6}){2,4}|\d{4}(?:-\d{3,6}){2,4})${'$'}"""
        )
        private val PROVIDER_REGEX_LIST = listOf(
            // American Express
            Regex("^3[47]\\d{13}$"),
            // Diners Club
            Regex("^3(?:0[0-5]|[68]\\d)\\d{11,13}$"),
            // Discover
            Regex("^6(?:011|5\\d{2})\\d{12,15}$"),
            // JCB
            Regex("^(?:2131|1800|35\\d{3})\\d{11}$"),
            // Mastercard
            Regex("^(?:5[1-5]\\d{2}|(?:222\\d|22[3-9]\\d|2[3-6]\\d{2}|27[01]\\d|2720))\\d{12}$"),
            // UnionPay
            Regex("^(?:6[27]\\d{14,17}|81\\d{14,17})$"),
            // Visa
            Regex("^4\\d{12}(?:\\d{3,6})?$")
        )
    }
}
