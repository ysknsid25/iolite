package iolite.personal

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Japanese phone number covering landline, mobile, IP phone, navi-dial, and toll-free formats.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - Mobile (11 digits): `070`/`080`/`090` prefix, with or without hyphens
 *   (e.g. `09012345678`, `090-1234-5678`).
 * - IP phone (11 digits): `050` prefix, with or without hyphens (e.g. `050-1234-5678`).
 * - Toll-free `0800` (11 digits): with or without hyphens (e.g. `0800-123-4567`).
 * - Navi-dial (10 digits): `0570` prefix.
 * - Toll-free `0120` (10 digits).
 * - Landline (10 digits): leading `0` plus area code variants
 *   (2 / 3 / 4 / 5 digit area codes), with or without hyphens.
 * - Hyphens are allowed only between digit groups; leading, trailing, and consecutive
 *   hyphens (`--`) are rejected, and characters other than digits and `-` are rejected.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. Hyphens are preserved.
 *
 * ```kotlin
 * val phone: String = JpPhoneNumber(" 090-1234-5678 ").parse()
 * // → "090-1234-5678"
 * ```
 *
 * Note: validation errors do **not** echo the input value, since phone numbers
 * are PII (see iolite's sensitivity policy).
 */
@Suppress("Indentation")
@JvmInline
value class JpPhoneNumber(private val value: String) : ValueObject<String> {
    /**
     * Validates the wrapped phone number and returns the trimmed form.
     *
     * @return the trimmed phone number (hyphens preserved as written).
     * @throws IoliteException with [target = JpPhoneNumber][IoliteException.Target.JpPhoneNumber] and:
     *         - [rule = Characters][IoliteException.Rule.Characters] — input contains characters
     *           other than digits / hyphens, or has bad hyphen placement.
     *         - [rule = Format][IoliteException.Rule.Format] — input does not match any of the
     *           supported phone-number kinds (landline / mobile / IP phone / navi-dial / toll-free).
     *
     *         Error messages do not echo the input.
     */
    override fun parse(): String {
        val normalized = value.trim()

        ioliteRequire(
            target = IoliteException.Target.JpPhoneNumber,
            rule = IoliteException.Rule.Characters,
            condition = !normalized.startsWith("-") &&
                !normalized.endsWith("-") &&
                !normalized.contains("--") &&
                normalized.all { it.isDigit() || it == '-' },
        ) {
            "Invalid format: contains invalid characters or invalid hyphen usage"
        }

        val digitsOnly = normalized.replace("-", "")

        val isValid = when {
            // フリーダイヤル (0800) (11桁)
            digitsOnly.startsWith("0800") &&
                tollFree0800DigitsRegex.matches(digitsOnly) -> tollFree0800FormatRegex.matches(normalized)
            // 携帯電話 (11桁)
            mobileDigitsRegex.matches(digitsOnly) -> mobileFormatRegex.matches(normalized)
            // IP電話 (11桁)
            ipPhoneDigitsRegex.matches(digitsOnly) -> ipPhoneFormatRegex.matches(normalized)
            // ナビダイヤル (10桁)
            naviDialDigitsRegex.matches(digitsOnly) -> naviDialFormatRegex.matches(normalized)
            // フリーダイヤル (0120) (10桁)
            tollFree0120DigitsRegex.matches(digitsOnly) -> tollFree0120FormatRegex.matches(normalized)
            // 固定電話 (10桁)
            landlineDigitsRegex.matches(digitsOnly) -> landlineFormatRegex.matches(normalized)
            // 上記のいずれにも合致しない
            else -> false
        }

        ioliteRequire(
            target = IoliteException.Target.JpPhoneNumber,
            rule = IoliteException.Rule.Format,
            condition = isValid,
        ) {
            "Invalid Japanese Phone Number"
        }
        return normalized
    }

    /**
     * Returns a masked representation that exposes only the last 4 digits, e.g.
     * `JpPhoneNumber(***-****-5678)`. The exact mask format is **not** part of
     * the public API contract and may change.
     */
    override fun toString(): String {
        val digits = value.filter { it.isDigit() }
        val last4 = digits.takeLast(LAST_VISIBLE_DIGITS).padStart(LAST_VISIBLE_DIGITS, '*')
        return "JpPhoneNumber(***-****-$last4)"
    }

    companion object {
        private const val LAST_VISIBLE_DIGITS = 4

        // --- 数字のみの文字列を検証するための正規表現 ---
        private val mobileDigitsRegex = Regex("""^0(70|80|90)\d{8}$""")
        private val ipPhoneDigitsRegex = Regex("""^050\d{8}$""")
        private val tollFree0800DigitsRegex = Regex("""^0800\d{7}$""")
        private val naviDialDigitsRegex = Regex("""^0570\d{6}$""")
        private val tollFree0120DigitsRegex = Regex("""^0120\d{6}$""")
        private val landlineDigitsRegex = Regex("""^0\d{9}$""")

        private val mobileFormatRegex = Regex("""^0(70|80|90)(\d{8}|-\d{4}-\d{4})$""")
        private val ipPhoneFormatRegex = Regex("""^050(\d{8}|-\d{4}-\d{4})$""")
        private val tollFree0800FormatRegex = Regex("""^0800(\d{7}|-\d{3}-\d{4})$""")
        private val naviDialFormatRegex = Regex("""^0570(\d{6}|-\d{6}|-\d{3}-\d{3})$""")
        private val tollFree0120FormatRegex = Regex("""^0120(\d{6}|-\d{3}-\d{3})$""")
        private val landlineFormatRegex = Regex(
            """^(""" +
                // ハイフンなし (例: 0312345678)
                """0\d{9}|""" +
                // 市外局番2桁 (例: 03-1234-5678)
                """0\d-\d{4}-\d{4}|""" +
                // 市外局番3桁 (例: 045-123-4567)
                """0\d{2}-\d{3}-\d{4}|""" +
                // 市外局番4桁 (例: 0465-12-3456)
                """0\d{3}-\d{2}-\d{4}|""" +
                // 市外局番5桁 (例: 01234-5-6789)
                """0\d{4}-\d-\d{4}""" +
                ")$"
        )
    }
}
