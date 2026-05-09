package network

import iolite.IoliteException
import iolite.network.HostName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class HostNameTest {
    @Test
    fun parseShouldSucceedForValidHostnames() {
        for (input in validHostNames) {
            val hostName = HostName(input)
            assertEquals(input.trim(), hostName.parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun parseShouldThrowExceptionForInvalidHostnames() {
        for (input in invalidHostNames) {
            val hostName = HostName(input)
            val exception = assertFailsWith<IoliteException>("Expected fail for input='$input'") {
                hostName.parse()
            }
            assertTrue(
                exception.message!!.contains("Invalid HostName"),
                "Unexpected message for input='$input': ${exception.message}"
            )
        }
    }

    @Test
    fun safeParseShouldSucceedForValidHostnames() {
        for (input in validHostNames) {
            val hostName = HostName(input)
            assertTrue(hostName.safeParse().isSuccess, "Expected success for input='$input'")
            assertEquals(input.trim(), hostName.safeParse().getOrNull())
        }
    }

    @Test
    fun safeParseShouldReturnNullForInvalidHostnames() {
        for (input in invalidHostNames) {
            val hostName = HostName(input)
            assertTrue(hostName.safeParse().isFailure, "Expected failure for input='$input'")
        }
    }

    companion object {
        private val validHostNames = listOf(
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
            "my-host.example.local",
        )

        private val invalidHostNames = listOf(
            ".example.com",
            "example..com",
            "example.com.",
            "ex!ample.com",
            "example,com",
            "a..b",
            "",
            "..",
        )
    }
}
