package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * HTTP(S) URL with a public domain and an optional port / path component.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - Scheme: `http://` or `https://`. Other schemes (`ftp`, `mailto`, …) are rejected.
 * - Host: a domain whose first / last label characters are alphanumeric and that ends in
 *   a TLD of at least two alphabetic characters (e.g. `example.com`, `sub.example.co.jp`).
 *   Single-label hosts (`localhost`) and raw IP addresses are not accepted by this VO —
 *   use [HostName] / [IpV4] / [IpV6] instead.
 * - Optional port: `:` followed by 1–5 digits.
 * - Optional path / query / fragment: any sequence of unreserved / sub-delims / `/?#[]@!$&'()*+,;=` characters.
 * - Total length must not exceed [MAX_URL_LENGTH] (2048).
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The rest is preserved verbatim
 *   (no scheme lower-casing, no path canonicalization).
 *
 * ```kotlin
 * Url("https://example.com").parse()                     // → "https://example.com"
 * Url("https://api.example.com:8443/v1/users?id=42").parse()
 * Url("ftp://example.com").parse()                       // throws IoliteException (Format)
 * ```
 */
@JvmInline
public value class Url(private val value: String) : ValueObject<String> {

    /**
     * Validates the wrapped URL and returns the trimmed form.
     *
     * @return the trimmed URL string.
     * @throws IoliteException with [target = Url][IoliteException.Target.Url]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         a well-formed `http(s)://` URL or exceeds [MAX_URL_LENGTH] characters.
     */
    @Suppress("MaxLineLength")
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.Url,
            rule = IoliteException.Rule.Format,
            condition = Regex(
                "^(https?)://(?!-)[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9](\\.[a-zA-Z]{2,})+(:[0-9]{1,5})?(/[a-zA-Z0-9\\-._~:/?#\\[\\]@!$&'()*+,;=]*)?$"
            ).matches(normalized) && normalized.length <= MAX_URL_LENGTH,
        ) {
            "Invalid URL: $value"
        }
        return normalized
    }

    /**
     * Returns `Url(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "Url($value)"

    private companion object {
        /** Maximum accepted URL length (inclusive), matching common browser limits. */
        private const val MAX_URL_LENGTH = 2048
    }
}
