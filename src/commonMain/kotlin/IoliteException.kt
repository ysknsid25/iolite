package iolite

/**
 * Base exception thrown by all iolite Value Objects when validation fails.
 *
 * Extends [IllegalArgumentException] so existing
 * `try { ... } catch (e: IllegalArgumentException)` code keeps working,
 * but library users SHOULD prefer catching this type to distinguish
 * iolite validation failures from other argument errors.
 *
 * @property target identifies the Value Object whose validation failed.
 * @property rule identifies the kind of rule that was violated.
 */
public open class IoliteException(
    public val target: Target,
    public val rule: Rule,
    message: String,
    cause: Throwable? = null,
) : IllegalArgumentException(message, cause) {

    /** Identifier of the Value Object that produced the validation failure. */
    public enum class Target {
        Date,
        DateTime,
        Time,
        Base64,
        Uuid,
        Cidr,
        Domain,
        HostName,
        IpV4,
        IpV6,
        MacAddress,
        Url,
        Age,
        CreditCardNumber,
        Email,
        JpPhoneNumber,
        JpPostalCode,
        AlphaNumericString,
        DecimalString,
        IntegerString,
        StringValueObject,
    }

    /** Identifier of the validation rule that was violated. */
    public enum class Rule {
        /** The input did not match the expected format / regex. */
        Format,

        /** The input was outside the accepted numeric range. */
        Range,

        /** The input failed the Luhn checksum (credit card). */
        Luhn,

        /** The input did not belong to a recognised provider. */
        Provider,

        /** The input contained disallowed characters. */
        Characters,

        /** The string was empty when a non-empty value was required. */
        NotEmpty,

        /** The string length was below the configured minimum. */
        Min,

        /** The string length exceeded the configured maximum. */
        Max,

        /** The string did not start with the required prefix. */
        StartWith,

        /** The string did not end with the required suffix. */
        EndWith,

        /** The string did not match a user-supplied regex. */
        Regex,

        /** A user-supplied predicate rejected the input. */
        CustomerValidation,
    }
}

/**
 * Throws [IoliteException] with the given [target] and [rule] when [condition] is `false`.
 *
 * Drop-in replacement for `kotlin.require` for iolite Value Object validation,
 * keeping the lazy-evaluated message contract.
 */
internal inline fun ioliteRequire(
    target: IoliteException.Target,
    rule: IoliteException.Rule,
    condition: Boolean,
    lazyMessage: () -> String,
) {
    if (!condition) {
        throw IoliteException(target = target, rule = rule, message = lazyMessage())
    }
}
