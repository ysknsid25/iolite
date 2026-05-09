![Maven Central Version](https://img.shields.io/maven-central/v/io.github.ysknsid25.iolite/iolite)
![Maven Central Last Update](https://img.shields.io/maven-central/last-update/io.github.ysknsid25.iolite/iolite)
![Static Badge](https://img.shields.io/badge/coverage-97.69%25-green)


# 🔮 iolite

 A generic Value Object library for Kotlin. Inspired by [Zod](https://github.com/colinhacks/zod). We never use 3rd party libraries.

A Value Object is an object whose equality is determined by the "values ​​of its attributes" rather than the object's "identifier."
In Domain-Driven Design (DDD), they are used to clearly model concepts and rules.

# 🌐 Supported platforms

iolite is published as a Kotlin Multiplatform library and runs on:

- JVM (Java 1.8+)
- Kotlin/JS (IR, browser & Node.js)
- Kotlin/Native — Linux x64
- Kotlin/Native — macOS x64 / arm64
- Kotlin/Native — iOS arm64 / simulator arm64 / x64

# ⚙ Features

view [iolite API](https://ysknsid25.github.io/iolite/)

## DateTime

- Date
- DateTime
- Time

## Encoding

- Base64

## ID

- UUID

## Network

- CIDR
- Domain
- HostName
- IPV4
- IPV6
- MacAddress
- URL

## Personal

- Age
- CreditCardNumber
- Email
- JP Phone Number
- JP Postal Code

## Strings

- AlphaNumeric
- Decimal
- Integer

# Basic usage

```
val email = Email("youremail@example.com")
```

You will primarily be using **parse** and **safeParse**, which are explained in more detail below.

## parse

Parse validates the input value provided and raises an exception if it is invalid.

```
try {
  val email: String = Email("youremail@example.com").parse()
}catch (e: IoliteException) {
  // Handle the exception if needed
}
```

## safeParse

safeParse also verifies that the given value is valid, but **returns a Result type** instead of an exception.

```
val email = Email("youremail@example.com").safeParse()
if(email.isFailure){
  // Handle the exception if needed
}
println(email.getOrNull()) // print "youremail@example.com"
```

## Exception handling

iolite follows the [Kotlin API guidelines for debuggability](https://kotlinlang.org/docs/api-guidelines-debuggability.html) and throws a single library-specific exception type for every validation failure.

- Every `parse()` call raises **`iolite.IoliteException`** (and only that type) when validation fails.
- `safeParse()` catches **only** `IoliteException` and converts it into `Result.failure`. Any other exception thrown from inside `parse()` (e.g. a bug in a custom validation lambda) is intentionally **not** swallowed, so genuine bugs are not hidden as validation failures.
- `IoliteException` extends `IllegalArgumentException` for source compatibility — existing `catch (e: IllegalArgumentException)` blocks keep working — but new code SHOULD catch `IoliteException` to distinguish iolite validation failures from other argument errors.
- Each exception carries structured fields so failures can be inspected without parsing message strings:
  - `target: IoliteException.Target` — which Value Object failed (e.g. `Email`, `CreditCardNumber`).
  - `rule: IoliteException.Rule` — which rule was violated (e.g. `Format`, `Range`, `Luhn`).

```kotlin
import iolite.IoliteException
import iolite.personal.Email

val result = Email("not-an-email").safeParse()
result.exceptionOrNull()?.let { e ->
    if (e is IoliteException) {
        println("validation failed: target=${e.target} rule=${e.rule}")
        // -> validation failed: target=Email rule=Format
    }
}
```

## StringValueObject

All value classes other than StringValueObject in the strings package inherit StringValueObject, and it is possible to check the values ​​using method chaining as shown below.

```
val stringVal = StringValueObject("prefix123suffix")
    .notEmpty()
    .startWith("prefix")
    .endWith("suffix")
    .min(10)
    .max(20)
    .regex(Regex("^[a-zA-Z0-9]+$"))
    .customerValidation(
        validation = { it.contains("123") },
        errorMessage = "Custom validation failed"
    )
    .parse()

val integerString = IntegerString("100000000")
    .parse() // can retlieve StringValueObject
    .notEmpty()
    .min(3)
    .max(10)
    .parse()
```

## CreditCard

You can validate these credit card brands.

- Amex
- Diners Club
- Discover
- JCB
- MasterCard
- Visa

```
val creditCardNumber = CreditCard("4111111111111111").parse()
```

this is same logic of [Valibot](https://github.com/fabian-hiller/valibot/blob/54c846ada01af06deccfbd56f68fca362c445fae/library/src/actions/creditCard/creditCard.ts#L118-L146). and By running them through [the same test cases](https://github.com/fabian-hiller/valibot/blob/main/library/src/actions/creditCard/creditCard.test.ts), we can ensure that they are of the same quality.

# Installation

## Pure JVM / Android / Server-side Kotlin

```kotlin
implementation("io.github.ysknsid25.iolite:iolite:{version}")
```

## Kotlin Multiplatform

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.ysknsid25.iolite:iolite:{version}")
        }
    }
}
```

refer [here](https://mvnrepository.com/artifact/io.github.ysknsid25.iolite/iolite)

# 🛠 Development

You can use either IntelliJ IDEA (the project's primary IDE) or VSCode with a Dev Container.

## VSCode Dev Container

Prerequisites:

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (or Docker Engine on Linux)
- [VSCode](https://code.visualstudio.com/) with the [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) extension

Steps:

1. Clone the repository and open the folder in VSCode.
2. When prompted, choose **"Reopen in Container"** (or run the `Dev Containers: Reopen in Container` command).
3. The container builds automatically. On first start it installs Claude Code, configures the git hooks, and warms up the Gradle cache.
4. From a container terminal, you can run the same tasks CI runs:

   ```bash
   ./gradlew detekt         # static analysis
   ./gradlew allTests       # common + JVM tests
   ./gradlew koverXmlReport # coverage report
   ```

What the container provides:

- Temurin JDK 17 (LTS) with Gradle wrapper-driven builds
- Pre-configured VSCode extensions: Kotlin (`fwcd.kotlin`), Gradle, Java test runners, YAML, EditorConfig, and Claude Code
- Claude Code CLI installed via the official `curl -fsSL https://claude.ai/install.sh | bash` script. Your host `~/.claude` and `~/.claude.json` are bind-mounted into the container so credentials, settings, and history are shared with your host setup.
- Auto-configured `pre-commit` git hook (`.githooks/pre-commit` activated via `core.hooksPath`)

## IntelliJ IDEA

Open the project folder in IntelliJ — Gradle import runs automatically. After import, enable the pre-commit hook:

```bash
chmod +x .githooks/pre-commit
git config core.hooksPath .githooks
```

## Common Gradle tasks

All tasks are run via the bundled Gradle wrapper (`./gradlew`) regardless of whether you use IntelliJ or a Dev Container.

### Build

```bash
./gradlew build              # full build (compile + test + check)
./gradlew clean              # remove build artifacts
./gradlew assemble           # compile only, skip tests
```

### Test

```bash
./gradlew allTests           # run all KMP tests (commonTest + jvmTest)
./gradlew jvmTest            # run JVM tests only (JUnit 5 + Konsist)
./gradlew check              # run all verification tasks (test + detekt)
```

### Static analysis (Detekt)

```bash
./gradlew detekt             # run Detekt with auto-correct enabled
                             # report: reports/detekt.html
```

Detekt configuration: `config/detekt/detekt.yml`.

### Coverage (Kover)

```bash
./gradlew koverHtmlReport    # generate HTML coverage report
                             # report: build/reports/kover/html/index.html
./gradlew koverXmlReport     # generate XML coverage report (used by CI)
                             # report: build/reports/kover/report.xml
./gradlew koverVerify        # fail the build if coverage is below the threshold
```

### Documentation (Dokka)

```bash
./gradlew dokkaHtml          # generate API docs into docs/
```

### Discovering tasks

```bash
./gradlew tasks              # list all available tasks
./gradlew help --task <name> # show details for a specific task
```

# ✨ Star History ✨

[![Star History Chart](https://api.star-history.com/svg?repos=ysknsid25/iolite&type=Date)](https://star-history.com/#bytebase/star-history&Date)

# 📻 Origin of project name

- Inspired by Zod. Zod's icon is a blue gem, so I thought I'd use the gem's name.
- I thought of jewels with similar colors to Kotlin's brand color.
- Because [my favorite artist](https://www.youtube.com/watch?v=YPLPI-cs7xg) has a song with the same name.
