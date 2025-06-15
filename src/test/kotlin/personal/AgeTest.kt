package personal

import inorin.personal.Age
import inorin.strings.IntegerString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AgeTest {

    @Test
    fun `parse should throw exception for values below MIN_AGE`() {
        val invalidAge = Age(Age.MIN_AGE - 1)
        val exception = assertThrows<IllegalArgumentException> {
            invalidAge.parse()
        }
        assertEquals(
            "Invalid age: ${Age.MIN_AGE - 1}. Age must be between ${Age.MIN_AGE} and ${Age.MAX_AGE}.",
            exception.message
        )
    }

    @Test
    fun `parse should throw exception for values above MAX_AGE`() {
        val invalidAge = Age(Age.MAX_AGE + 1)
        val exception = assertThrows<IllegalArgumentException> {
            invalidAge.parse()
        }
        assertEquals(
            "Invalid age: ${Age.MAX_AGE + 1}. Age must be between ${Age.MIN_AGE} and ${Age.MAX_AGE}.",
            exception.message
        )
    }

    @Test
    fun `parse should return value for valid age within range`() {
        val validAge = Age(Age.MIN_AGE)
        assertEquals(Age.MIN_AGE, validAge.parse())

        val validAgeMax = Age(Age.MAX_AGE)
        assertEquals(Age.MAX_AGE, validAgeMax.parse())

        val validAgeMiddle = Age((Age.MIN_AGE + Age.MAX_AGE) / 2)
        assertEquals((Age.MIN_AGE + Age.MAX_AGE) / 2, validAgeMiddle.parse())
    }

    @Test
    fun `parse should work with IntegerCharacter converted to Age`() {
        val validIntegerString = IntegerString("25")
        val age = Age(validIntegerString.parse().parse().toInt())
        assertEquals(25, age.parse())
    }

    @Test
    fun `safeParse should return failure for invalidAgeBelow`() {
        val result = Age(Age.MIN_AGE - 1).safeParse()
        assertTrue(result.isFailure)
        assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    @Test
    fun `safeParse should return failure for invalidAgeAbove`() {
        val result = Age(Age.MAX_AGE + 1).safeParse()
        assertTrue(result.isFailure)
        assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    @Test
    fun `safeParse should return value for valid age within range`() {
        val validAgeMiddle = Age((Age.MIN_AGE + Age.MAX_AGE) / 2)
        val result = validAgeMiddle.safeParse()
        assertTrue(result.isSuccess)
        assertEquals((Age.MIN_AGE + Age.MAX_AGE) / 2, validAgeMiddle.safeParse().getOrThrow())
    }
}
