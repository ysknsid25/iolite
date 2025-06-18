package iolite.personal

import iolite.ValueObject

@JvmInline
value class JpPhoneNumber(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        val normalized = value.trim()

        require(
            !normalized.startsWith("-") &&
                    !normalized.endsWith("-") &&
                    !normalized.contains("--") &&
                    normalized.all { it.isDigit() || it == '-' }
        ) {
            "Invalid format: contains invalid characters or invalid hyphen usage in '$value'"
        }

        val digitsOnly = normalized.replace("-", "")

        val isValid = when {
            // フリーダイヤル (0800) (11桁)
            digitsOnly.startsWith("0800") && tollFree0800DigitsRegex.matches(digitsOnly) -> tollFree0800FormatRegex.matches(normalized)
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

        require(isValid) {
            "Invalid Japanese Phone Number: '$value'"
        }
        return normalized
    }

    companion object {
        // --- 数字のみの文字列を検証するための正規表現 ---
        private val mobileDigitsRegex = Regex("""^0(70|80|90)\d{8}$""")
        private val ipPhoneDigitsRegex = Regex("""^050\d{8}$""")
        private val tollFree0800DigitsRegex = Regex("""^0800\d{7}$""")
        private val naviDialDigitsRegex = Regex("""^0570\d{6}$""")
        private val tollFree0120DigitsRegex = Regex("""^0120\d{6}$""")
        private val landlineDigitsRegex = Regex("""^0\d{9}$""")

        // --- ハイフンを含めたフォーマットを検証するための正規表現 ---
        private val mobileFormatRegex = Regex("""^0(70|80|90)(\d{8}|-\d{4}-\d{4})$""")
        private val ipPhoneFormatRegex = Regex("""^050(\d{8}|-\d{4}-\d{4})$""")
        // 0800番号のハイフン区切りを XXX-XXXX に修正
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