package iolite.strings

import iolite.IoliteException
import iolite.ValueObject
import iolite.ioliteRequire
import kotlin.jvm.JvmInline

/**
 * General-purpose string value object that exposes a chainable, fluent
 * validation API (think Zod's `z.string()`).
 *
 * Unlike most VOs, [parse] performs **no validation** by itself — it just
 * unwraps the underlying string. Validations are opt-in: chain the rule
 * methods ([notEmpty], [min], [max], [startWith], [endWith], [regex],
 * [customerValidation]), each of which throws [IoliteException] on failure
 * and returns `this` to allow chaining.
 *
 * Normalization: none — the wrapped value is preserved exactly as supplied.
 *
 * ```kotlin
 * val name: String = StringValueObject("  Alice  ")
 *     .notEmpty()
 *     .min(2)
 *     .max(50)
 *     .parse()
 *
 * // Format constraint via regex
 * StringValueObject("hello-world").regex(Regex("^[a-z-]+\$")).parse()
 *
 * // Custom predicate
 * StringValueObject("abc").customerValidation(
 *     validation = { it.all(Char::isLowerCase) },
 *     errorMessage = "must be lowercase",
 * ).parse()
 * ```
 */
@JvmInline
public value class StringValueObject(private val value: String) : ValueObject<String> {

    /**
     * Returns the wrapped string unchanged.
     *
     * Apply validations by chaining rule methods (e.g. [notEmpty], [min], [regex])
     * before calling `parse`. This method itself never throws.
     *
     * @return the wrapped string, exactly as supplied.
     */
    override fun parse(): String {
        return value
    }

    /**
     * Asserts that the wrapped value is not empty.
     *
     * @return `this`, for chaining.
     * @throws IoliteException with [rule = NotEmpty][IoliteException.Rule.NotEmpty] if the value is empty.
     */
    public fun notEmpty(): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.NotEmpty,
            condition = value.isNotEmpty(),
        ) {
            "String value cannot be empty"
        }
        return this
    }

    /**
     * Asserts that the wrapped value's length is at least [threshold] characters.
     *
     * @param threshold the minimum required length, inclusive.
     * @return `this`, for chaining.
     * @throws IoliteException with [rule = Min][IoliteException.Rule.Min] if the length is below [threshold].
     */
    public fun min(threshold: Int): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.Min,
            condition = value.length >= threshold,
        ) {
            "Value $value is less than minimum threshold $threshold"
        }
        return this
    }

    /**
     * Asserts that the wrapped value's length is at most [threshold] characters.
     *
     * @param threshold the maximum allowed length, inclusive.
     * @return `this`, for chaining.
     * @throws IoliteException with [rule = Max][IoliteException.Rule.Max] if the length exceeds [threshold].
     */
    public fun max(threshold: Int): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.Max,
            condition = value.length <= threshold,
        ) {
            "Value $value is greater than maximum threshold $threshold"
        }
        return this
    }

    /**
     * Asserts that the wrapped value starts with [prefix].
     *
     * @return `this`, for chaining.
     * @throws IoliteException with [rule = StartWith][IoliteException.Rule.StartWith]
     *         if the value does not start with [prefix].
     */
    public fun startWith(prefix: String): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.StartWith,
            condition = value.startsWith(prefix),
        ) {
            "Value $value does not start with $prefix"
        }
        return this
    }

    /**
     * Asserts that the wrapped value ends with [suffix].
     *
     * @return `this`, for chaining.
     * @throws IoliteException with [rule = EndWith][IoliteException.Rule.EndWith]
     *         if the value does not end with [suffix].
     */
    public fun endWith(suffix: String): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.EndWith,
            condition = value.endsWith(suffix),
        ) {
            "Value $value does not end with $suffix"
        }
        return this
    }

    /**
     * Asserts that the wrapped value matches [regex] in full
     * (i.e. uses [String.matches], not a partial find).
     *
     * @return `this`, for chaining.
     * @throws IoliteException with [rule = Regex][IoliteException.Rule.Regex]
     *         if the value does not match [regex].
     */
    public fun regex(regex: Regex): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.Regex,
            condition = value.matches(regex),
        ) {
            "Value $value does not match regex pattern ${regex.pattern}"
        }
        return this
    }

    /**
     * Asserts that the wrapped value satisfies a caller-supplied predicate.
     *
     * @param validation predicate that returns `true` when the value is valid.
     * @param errorMessage message used in the [IoliteException] when [validation] returns `false`.
     * @return `this`, for chaining.
     * @throws IoliteException with [rule = CustomerValidation][IoliteException.Rule.CustomerValidation]
     *         if [validation] returns `false`.
     */
    public fun customerValidation(validation: (String) -> Boolean, errorMessage: String): StringValueObject {
        ioliteRequire(
            target = IoliteException.Target.StringValueObject,
            rule = IoliteException.Rule.CustomerValidation,
            condition = validation(value),
        ) {
            errorMessage
        }
        return this
    }

    /**
     * Returns `StringValueObject(value)`. The format is **not** part of the
     * public API contract and may change.
     */
    override fun toString(): String = "StringValueObject($value)"
}
