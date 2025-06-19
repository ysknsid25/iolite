package id

import iolite.id.Uuid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class UuidTest {
    @ParameterizedTest
    @MethodSource("validUUIDs")
    fun `valid UUIDs should parse successfully`(input: String) {
        assertEquals(input, Uuid(input).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidUUIDs")
    fun `invalid UUIDs should throw exceptions`(input: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { Uuid(input).parse() }
    }

    @ParameterizedTest
    @MethodSource("validUUIDs")
    fun `safeParse should return success for valid inputs`(input: String) {
        val result = Uuid(input).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(input, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidUUIDs")
    fun `safeParse should return failure for invalid inputs`(input: String) {
        val result = Uuid(input).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validUUIDs() = listOf(
            "9491d710-3185-4e06-bea0-6a2f275345e0",
            "d89e7b01-7598-ed11-9d7a-0022489382fd",
            "00000000-0000-0000-0000-000000000000",
            "b3ce60f8-e8b9-40f5-1150-172ede56ff74",
            "92e76bf9-28b3-4730-cd7f-cb6bc51f8c09",
        )

        @JvmStatic
        fun invalidUUIDs() = listOf(
            "9491d710-3185-4e06-bea0-6a2f275345e0X",
        )
    }
}
