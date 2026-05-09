package id

import iolite.IoliteException
import iolite.id.Uuid
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UuidTest {
    @Test
    fun validUUIDsShouldParseSuccessfully() {
        for (input in validUUIDs) {
            assertEquals(input, Uuid(input).parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidUUIDsShouldThrowExceptions() {
        for (input in invalidUUIDs) {
            assertFailsWith<IoliteException>("Expected fail for input='$input'") {
                Uuid(input).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (input in validUUIDs) {
            val result = Uuid(input).safeParse()
            assertTrue(result.isSuccess, "Expected success for input='$input'")
            assertEquals(input, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (input in invalidUUIDs) {
            val result = Uuid(input).safeParse()
            assertTrue(result.isFailure, "Expected failure for input='$input'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    @Test
    fun toStringShouldRenderClassNameAndValue() {
        assertEquals(
            "Uuid(9491d710-3185-4e06-bea0-6a2f275345e0)",
            Uuid("9491d710-3185-4e06-bea0-6a2f275345e0").toString()
        )
    }

    companion object {
        private val validUUIDs = listOf(
            "9491d710-3185-4e06-bea0-6a2f275345e0",
            "d89e7b01-7598-ed11-9d7a-0022489382fd",
            "00000000-0000-0000-0000-000000000000",
            "b3ce60f8-e8b9-40f5-1150-172ede56ff74",
            "92e76bf9-28b3-4730-cd7f-cb6bc51f8c09",
        )

        private val invalidUUIDs = listOf(
            "9491d710-3185-4e06-bea0-6a2f275345e0X",
        )
    }
}
