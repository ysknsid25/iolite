package network

import iolite.network.Domain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class DomainTest {

    @ParameterizedTest
    @MethodSource("validDomains")
    fun `parse should succeed for valid domains`(input: String) {
        val domain = Domain(input)
        assertDoesNotThrow { domain.parse() }
        assertEquals(input.trim(), domain.parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDomains")
    fun `parse should throw exception for invalid domains`(input: String) {
        val domain = Domain(input)
        val exception = assertThrows<IllegalArgumentException> { domain.parse() }
        assertTrue(exception.message!!.contains("Invalid Domain"))
    }

    @ParameterizedTest
    @MethodSource("validDomains")
    fun `safeParse should succeed for valid domains`(input: String) {
        val domain = Domain(input)
        assertDoesNotThrow { domain.safeParse() }
        assertTrue(domain.safeParse().isSuccess)
        assertEquals(input.trim(), domain.safeParse().getOrNull())
    }

    @ParameterizedTest
    @MethodSource("invalidDomains")
    fun `safeParse should return failure for invalid domains`(input: String) {
        val domain = Domain(input)
        assertTrue(domain.safeParse().isFailure)
    }

    companion object {
        @JvmStatic
        fun validDomains(): Stream<String> = Stream.of(
            "example.com",
            "sub.example.com",
            "a.co",
            "my-domain.org",
            "123-abc.net",
            "abc123.io",
            "example-domain.co.uk",
            "test.example.travel",
            "foo.bar.baz.info"
        )

        @JvmStatic
        fun invalidDomains(): Stream<String> = Stream.of(
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
            ".."
        )
    }
}
