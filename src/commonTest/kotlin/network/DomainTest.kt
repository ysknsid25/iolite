package network

import iolite.network.Domain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DomainTest {
    @Test
    fun parseShouldSucceedForValidDomains() {
        for (input in validDomains) {
            val domain = Domain(input)
            assertEquals(input.trim(), domain.parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun parseShouldThrowExceptionForInvalidDomains() {
        for (input in invalidDomains) {
            val domain = Domain(input)
            val exception = assertFailsWith<IllegalArgumentException>("Expected fail for input='$input'") {
                domain.parse()
            }
            assertTrue(
                exception.message!!.contains("Invalid Domain"),
                "Unexpected message for input='$input': ${exception.message}"
            )
        }
    }

    @Test
    fun safeParseShouldSucceedForValidDomains() {
        for (input in validDomains) {
            val domain = Domain(input)
            assertTrue(domain.safeParse().isSuccess, "Expected success for input='$input'")
            assertEquals(input.trim(), domain.safeParse().getOrNull())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidDomains() {
        for (input in invalidDomains) {
            val domain = Domain(input)
            assertTrue(domain.safeParse().isFailure, "Expected failure for input='$input'")
        }
    }

    companion object {
        private val validDomains = listOf(
            "example.com",
            "sub.example.com",
            "a.co",
            "my-domain.org",
            "123-abc.net",
            "abc123.io",
            "example-domain.co.uk",
            "test.example.travel",
            "foo.bar.baz.info",
        )

        private val invalidDomains = listOf(
            "example",
            ".example.com",
            "example..com",
            "example.com.",
            "-example.com",
            "example-.com",
            "exa_mple.com",
            "ex!ample.com",
            "a..b",
            "xn--d1acpjx3f.xn--p1ai",
            "",
            " ",
            "..",
        )
    }
}
