package iolite

/**
 * Marker interface implemented by every iolite Value Object.
 *
 * Implementations wrap a primitive (typically [String] or [Int]) and expose
 * validation through [parse] / [safeParse]. Each VO is responsible for two things:
 *
 * 1. **Validation** — rejecting inputs that violate its format / range / domain rules
 *    by throwing [IoliteException] (use the `ioliteRequire(...)` helper internally).
 * 2. **Normalization** — returning a canonical form of accepted inputs (e.g. trimmed
 *    whitespace, lower-cased local part, etc.). The exact normalization is documented
 *    on each implementing class.
 *
 * Typical usage:
 *
 * ```kotlin
 * // Throwing variant: parse the value and use the normalized form.
 * val email: String = Email("  Alice@Example.com ").parse()  // → "alice@example.com"
 *
 * // Result-based variant: never throws.
 * val result: Result<String> = Email(rawInput).safeParse()
 * result.fold(
 *     onSuccess = { normalized -> /* use it */ },
 *     onFailure = { error -> /* error is an IoliteException */ },
 * )
 * ```
 *
 * @param T the type of the validated, normalized value (most VOs use `String`,
 *          some use `Int` or another VO type).
 *
 * @see IoliteException for the exception thrown on validation failure.
 */
public interface ValueObject<T> {
    /**
     * Validates the wrapped value and returns the normalized form.
     *
     * Each implementation documents the exact accepted format and normalization rules.
     *
     * @return the normalized value (e.g. trimmed, lowercased, or otherwise canonicalised).
     * @throws IoliteException if the wrapped value violates the implementation's rules.
     */
    public fun parse(): T

    /**
     * Result-based variant of [parse] that never throws.
     *
     * Catches only [IoliteException] — other exceptions (e.g. `OutOfMemoryError`)
     * still propagate. Callers can branch on the failure's
     * [IoliteException.target] / [IoliteException.rule] without parsing message strings.
     *
     * ```kotlin
     * Email("not-an-email").safeParse().fold(
     *     onSuccess = { /* unreachable here */ },
     *     onFailure = { error -> /* error is an IoliteException with target=Email, rule=Format */ },
     * )
     * ```
     *
     * @return [Result.success] holding the normalized value, or [Result.failure]
     *         wrapping the [IoliteException] thrown by [parse].
     */
    public fun safeParse(): Result<T> {
        return try {
            Result.success(parse())
        } catch (e: IoliteException) {
            Result.failure(e)
        }
    }
}
