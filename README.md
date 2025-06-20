![Maven Central Version](https://img.shields.io/maven-central/v/io.github.ysknsid25.iolite/iolite)
![Maven Central Last Update](https://img.shields.io/maven-central/last-update/io.github.ysknsid25.iolite/iolite)
![Static Badge](https://img.shields.io/badge/coverage-97.69%25-green)


# 🔮 iolite

 A generic Value Object library for Kotlin. Inspired by [Zod](https://github.com/colinhacks/zod). We never use 3rd party libraries.

A Value Object is an object whose equality is determined by the "values ​​of its attributes" rather than the object's "identifier."
In Domain-Driven Design (DDD), they are used to clearly model concepts and rules.

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
}catch (e: IllegalArgumentException) {
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

```
implementation("io.github.ysknsid25.iolite:iolite:{version}")
```

refer [here](https://mvnrepository.com/artifact/io.github.ysknsid25.iolite/iolite)

# ✨ Star History ✨

[![Star History Chart](https://api.star-history.com/svg?repos=ysknsid25/iolite&type=Date)](https://star-history.com/#bytebase/star-history&Date)

# 📻 Origin of project name

- Inspired by Zod. Zod's icon is a blue gem, so I thought I'd use the gem's name.
- I thought of jewels with similar colors to Kotlin's brand color.
- Because [my favorite artist](https://www.youtube.com/watch?v=YPLPI-cs7xg) has a song with the same name.
