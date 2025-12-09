package network

import iolite.network.HostName
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class HostNameTest {

    @ParameterizedTest
    @MethodSource("validHostNames")
    fun `parse should succeed for valid hostnames`(input: String) {
        val hostName = HostName(input)
        assertDoesNotThrow { hostName.parse() }
        assertEquals(input.trim(), hostName.parse())
    }

    @ParameterizedTest
    @MethodSource("invalidHostNames")
    fun `parse should throw exception for invalid hostnames`(input: String) {
        val hostName = HostName(input)
        val exception = assertThrows<IllegalArgumentException> { hostName.parse() }
        assertTrue(exception.message!!.contains("Invalid HostName"))
    }

    @ParameterizedTest
    @MethodSource("validHostNames")
    fun `safeParse should succeed for valid hostnames`(input: String) {
        val hostName = HostName(input)
        assertDoesNotThrow { hostName.safeParse() }
        assertTrue(hostName.safeParse().isSuccess)
        assertEquals(input.trim(), hostName.safeParse().getOrNull())
    }

    @ParameterizedTest
    @MethodSource("invalidHostNames")
    fun `safeParse should return null for invalid hostnames`(input: String) {
        val hostName = HostName(input)
        assertTrue(hostName.safeParse().isFailure)
    }

    companion object {
        @JvmStatic
        fun validHostNames(): Stream<String> = Stream.of(
            "localhost",
            "example",
            "example.com",
            "sub.example.com",
            "my-host",
            "a-b-c",
            "a123.b456.c789",
            "123.456.789.000",
            "test-domain",
            "test.domain-name",
            "my-host.local",
            "my-host.example.local"
        )

        @JvmStatic
        fun invalidHostNames(): Stream<String> = Stream.of(
            ".example.com",
            "example..com",
            "example.com.",
            "ex!ample.com",
            "example,com",
            "a..b",
            "",
            ".."
        )
    }
}
