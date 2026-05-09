package iolite.personal

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Credit card number with major-brand provider detection and Luhn checksum validation.
 *
 * Accepts:
 * - 14–19 contiguous digits (e.g. `4242424242424242`), or
 * - 4-digit groups separated by either a single space or a single hyphen
 *   (e.g. `4242 4242 4242 4242` or `4242-4242-4242-4242`).
 * - The digits-only form must match one of the recognised provider patterns
 *   (American Express, Diners Club, Discover, JCB, Mastercard, UnionPay, Visa).
 * - The digits-only form must pass the Luhn checksum.
 *
 * Normalization: none — the value is returned exactly as supplied (separators preserved).
 *
 * ```kotlin
 * val card: String = CreditCardNumber("4242 4242 4242 4242").parse()
 * // → "4242 4242 4242 4242" (preserved as-is)
 * ```
 *
 * Note: validation errors do **not** echo the input value, since card numbers
 * are PCI-sensitive (see iolite's sensitivity policy). The provider list is
 * modelled after Valibot's `creditCard` validator.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Luhn_algorithm">Luhn algorithm</a>
 */
@JvmInline
value class CreditCardNumber(private val value: String) : ValueObject<String> {

    /**
     * Validates the wrapped credit card number and returns it unchanged.
     *
     * Validation runs in three stages: format, provider, then Luhn check.
     *
     * @return the credit card number with separators preserved (no normalization).
     * @throws IoliteException with [target = CreditCardNumber][IoliteException.Target.CreditCardNumber]
     *         and one of:
     *         - [rule = Format][IoliteException.Rule.Format] — input does not match the digit / separator pattern.
     *         - [rule = Provider][IoliteException.Rule.Provider] — input does not match any recognised brand.
     *         - [rule = Luhn][IoliteException.Rule.Luhn] — input fails the Luhn checksum.
     *
     *         Error messages do not echo the input.
     */
    override fun parse(): String {
        ioliteRequire(
            target = IoliteException.Target.CreditCardNumber,
            rule = IoliteException.Rule.Format,
            condition = CREDIT_CARD_REGEX.matches(value),
        ) {
            "Invalid credit card format"
        }
        val sanitizedNumber = value.replace(SANITIZE_REGEX, "")
        ioliteRequire(
            target = IoliteException.Target.CreditCardNumber,
            rule = IoliteException.Rule.Provider,
            condition = PROVIDER_REGEX_LIST.any { it.matches(sanitizedNumber) },
        ) {
            "Unknown card provider"
        }
        ioliteRequire(
            target = IoliteException.Target.CreditCardNumber,
            rule = IoliteException.Rule.Luhn,
            condition = isLuhnAlgo(sanitizedNumber),
        ) {
            "Invalid credit card number (Luhn check failed)"
        }
        return value
    }

    /**
     * Returns a masked representation that exposes only the last 4 digits, e.g.
     * `CreditCardNumber(****-****-****-1234)`. The exact mask format is **not**
     * part of the public API contract and may change.
     */
    override fun toString(): String {
        val digits = value.filter { it.isDigit() }
        val last4 = digits.takeLast(LAST_VISIBLE_DIGITS).padStart(LAST_VISIBLE_DIGITS, '*')
        return "CreditCardNumber(****-****-****-$last4)"
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
        private const val LAST_VISIBLE_DIGITS = 4
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
