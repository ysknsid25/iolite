package iolite.network

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * Permissive hostname covering both single-label names (e.g. `localhost`, internal hostnames)
 * and dotted multi-label names.
 *
 * Accepts (after surrounding whitespace is trimmed):
 * - One or more labels separated by `.`. Each label may contain alphanumerics and hyphens.
 * - Single-label hostnames such as `localhost` or `db-primary` are accepted.
 * - This is intentionally laxer than [Domain] — use [Domain] when an FQDN with a real TLD is required.
 *
 * Normalization:
 * - Surrounding whitespace is removed via `trim()`. The hostname itself is preserved
 *   (no case folding).
 *
 * ```kotlin
 * HostName("localhost").parse()        // → "localhost"
 * HostName("db.internal").parse()      // → "db.internal"
 * ```
 */
@JvmInline
value class HostName(private val value: String) : ValueObject<String> {

    /**
     * Validates the wrapped hostname and returns the trimmed form.
     *
     * @return the trimmed hostname string.
     * @throws IoliteException with [target = HostName][IoliteException.Target.HostName]
     *         and [rule = Format][IoliteException.Rule.Format] if the value is not
     *         a well-formed hostname.
     */
    override fun parse(): String {
        val normalized = value.trim()
        ioliteRequire(
            target = IoliteException.Target.HostName,
            rule = IoliteException.Rule.Format,
            condition = Regex("^([a-zA-Z0-9-]+\\.)*[a-zA-Z0-9-]+\$").matches(normalized),
        ) {
            "Invalid HostName: $value"
        }
        return normalized
    }

    /**
     * Returns `HostName(value)`. The format is **not** part of the public API
     * contract and may change.
     */
    override fun toString(): String = "HostName($value)"
}
