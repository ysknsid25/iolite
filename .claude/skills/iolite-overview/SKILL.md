---
name: iolite-overview
description: Use this at the START of any task on the iolite repository to load project structure, conventions, and the canonical Value Object layout — avoids re-reading every VO/test file. Trigger when the user asks to add/modify a Value Object, error message, validation rule, or test in this repo.
---

# iolite project overview

Kotlin Multiplatform Value Object library inspired by Zod. No 3rd-party runtime deps.
Targets: JVM (Java 1.8+), JS (IR), Native (Linux x64, macOS x64/arm64, iOS arm64/sim arm64/x64).

## Top-level layout

```
src/
  commonMain/kotlin/
    ValueObject.kt            # interface ValueObject<T> { parse(): T; safeParse(): Result<T> }
    IoliteException.kt        # IllegalArgumentException subclass + ioliteRequire(target, rule, condition, lazyMessage)
    datetime/                 # Date, DateTime, Time
    encoding/                 # Base64
    id/                       # Uuid
    network/                  # Cidr, Domain, HostName, IpV4, IpV6, MacAddress, Url
    personal/                 # Age, CreditCardNumber, Email, JpPhoneNumber, JpPostalCode  (PII / sensitive)
    strings/                  # AlphaNumericString, DecimalString, IntegerString, StringValueObject
  commonTest/kotlin/<package>/<Type>Test.kt   # one test file per VO
  jvmTest/kotlin/ValueObjectRuleTest.kt       # Konsist architecture rules
config/detekt/detekt.yml      # Detekt config
.githooks/pre-commit          # activated via core.hooksPath
```

## Value Object canonical pattern

Every VO is an `@JvmInline value class` (or plain `class` if it has multiple parameters like `DateTime`/`Time`)
implementing `ValueObject<T>`. The primary-constructor parameter is **always named `value`** (Konsist enforces this).

```kotlin
@JvmInline
value class Foo(private val value: String) : ValueObject<String> {
    override fun parse(): String {
        ioliteRequire(
            target = IoliteException.Target.Foo,
            rule = IoliteException.Rule.Format,
            condition = SOME_REGEX.matches(value),
        ) {
            "Invalid Foo: $value"   // NB: see Sensitivity policy below
        }
        return value
    }

    companion object { /* regex constants — must be the LAST declaration in the class (Konsist enforces this) */ }
}
```

`safeParse()` is provided by the interface and only catches `IoliteException`.

## IoliteException

- Extends `IllegalArgumentException` for source compatibility.
- Carries `target: Target` and `rule: Rule` enums so callers can branch without parsing message strings.
- Adding a new VO requires adding its name to `IoliteException.Target` and any new rule kind to `IoliteException.Rule`.
- Throw via the `ioliteRequire(target, rule, condition) { lazyMessage }` helper — never `require` / direct `throw`.

## Sensitivity policy (PII / PCI)

The `personal/` package handles sensitive data. Two rules apply to anything in `personal/`:

1. **Error messages must NOT echo the input value.** The `ioliteRequire` lazyMessage should describe what
   failed (e.g. `"Invalid email address"`) without interpolating `$value`. Other packages (`network/`, `id/`,
   `encoding/`, `strings/`, `datetime/`) keep `$value` for debuggability.
2. **`toString()` must mask sensitive data** for the three high-sensitivity VOs:
   - `Email` → `Email(j***@example.com)` (first char of local part + `***` + domain)
   - `CreditCardNumber` → `CreditCardNumber(****-****-****-1234)` (last 4 digits)
   - `JpPhoneNumber` → `JpPhoneNumber(***-****-1234)` (last 4 digits)
   - All other VOs (including `Age`, `JpPostalCode`, and everything outside `personal/`) use plain
     `ClassName(value)` toString. The masking decision table is the authority — don't re-debate per VO.
   - For multi-state VOs (`DateTime`, `Time`), include all state: `DateTime(value=…, precision=…, …)`.

When adding a VO to `personal/`, follow both rules and add a `toString` test asserting masked output.

## Konsist rules (jvmTest)

`src/jvmTest/kotlin/ValueObjectRuleTest.kt` enforces:

- companion object is the last declaration in the class
- every value class has a primary-constructor parameter named `value`
- every value class implements `ValueObject`

When adding new structural conventions (e.g. "personal VOs must override toString"), add them here.

## Build & test

```bash
./gradlew detekt             # static analysis  → reports/detekt.html
./gradlew allTests           # commonTest + jvmTest (KMP)
./gradlew jvmTest            # JVM only (includes Konsist)
./gradlew check              # test + detekt
./gradlew koverHtmlReport    # coverage  → build/reports/kover/html/index.html
./gradlew dokkaHtml          # API docs → docs/
```

`./gradlew check` is the canonical "is everything green" command. Detekt has `@Suppress` annotations
sprinkled for long regexes (`MaxLineLength`) and Luhn arithmetic (`MagicNumber`); follow the existing pattern.

## Test conventions

Per VO test file (`<package>/<Type>Test.kt`) typically has:

- `valid… should parse successfully` — iterate a list, assert `parse()` returns the (possibly normalized) value
- `invalid… should throw` — iterate a list, assert `assertFailsWith<IoliteException>`
- `safeParse should return Success/Failure` — Result-based variant
- For VOs that surface message details, additionally assert `exception.message`/`exception.target`/`exception.rule`

When changing error messages or `toString()` formats, grep `commonTest/` for `assertEquals(.*message` to find
assertions that must be updated alongside the implementation.

## Code style notes

- Detekt is enforced — keep regexes inside companion objects, suppress `MaxLineLength` only when the regex
  is genuinely unsplittable.
- Pre-commit hook runs detekt; don't bypass with `--no-verify`.
- The library publishes via Vanniktech Maven Publish; don't hand-edit `gradle.properties` POM coordinates.
