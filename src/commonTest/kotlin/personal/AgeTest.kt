package personal

import iolite.IoliteException
import iolite.personal.Age
import iolite.strings.IntegerString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AgeTest {

    @Test
    fun parseShouldThrowExceptionForValuesBelowMINAGE() {
        val invalidAge = Age(Age.MIN_AGE - 1)
        val exception = assertFailsWith<IoliteException> {
            invalidAge.parse()
        }
        assertEquals(
            "Invalid age: ${Age.MIN_AGE - 1}. Age must be between ${Age.MIN_AGE} and ${Age.MAX_AGE}.",
            exception.message
        )
    }

    @Test
    fun parseShouldThrowExceptionForValuesAboveMAXAGE() {
        val invalidAge = Age(Age.MAX_AGE + 1)
        val exception = assertFailsWith<IoliteException> {
            invalidAge.parse()
        }
        assertEquals(
            "Invalid age: ${Age.MAX_AGE + 1}. Age must be between ${Age.MIN_AGE} and ${Age.MAX_AGE}.",
            exception.message
        )
    }

    @Test
    fun parseShouldReturnValueForValidAgeWithinRange() {
        val validAge = Age(Age.MIN_AGE)
        assertEquals(Age.MIN_AGE, validAge.parse())

        val validAgeMax = Age(Age.MAX_AGE)
        assertEquals(Age.MAX_AGE, validAgeMax.parse())

        val validAgeMiddle = Age((Age.MIN_AGE + Age.MAX_AGE) / 2)
        assertEquals((Age.MIN_AGE + Age.MAX_AGE) / 2, validAgeMiddle.parse())
    }

    @Test
    fun parseShouldWorkWithIntegerCharacterConvertedToAge() {
        val validIntegerString = IntegerString("25")
        val age = Age(validIntegerString.parse().parse().toInt())
        assertEquals(25, age.parse())
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidAgeBelow() {
        val result = Age(Age.MIN_AGE - 1).safeParse()
        assertTrue(result.isFailure)
        assertFailsWith<IoliteException> { result.getOrThrow() }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidAgeAbove() {
        val result = Age(Age.MAX_AGE + 1).safeParse()
        assertTrue(result.isFailure)
        assertFailsWith<IoliteException> { result.getOrThrow() }
    }

    @Test
    fun safeParseShouldReturnValueForValidAgeWithinRange() {
        val validAgeMiddle = Age((Age.MIN_AGE + Age.MAX_AGE) / 2)
        val result = validAgeMiddle.safeParse()
        assertTrue(result.isSuccess)
        assertEquals((Age.MIN_AGE + Age.MAX_AGE) / 2, validAgeMiddle.safeParse().getOrThrow())
    }
}
