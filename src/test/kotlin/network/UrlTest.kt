package network

import iolite.network.Url
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class UrlTest {

    @ParameterizedTest
    @MethodSource("validUrls")
    fun `valid URLs should parse successfully`(url: String) {
        assertEquals(url, Url(url).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidUrls")
    fun `invalid URLs should throw exceptions`(url: String) {
        assertThrows<IllegalArgumentException> { Url(url).parse() }
    }

    @ParameterizedTest
    @MethodSource("validUrls")
    fun `safeParse should return success for valid inputs`(input: String) {
        val result = Url(input).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(input, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidUrls")
    fun `safeParse should return failure for invalid inputs`(input: String) {
        val result = Url(input).safeParse()
        assertTrue(result.isFailure)
        assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validUrls() = listOf(
            "https://example.com",
            "http://example.com",
            "https://example.com:8080",
            "http://example.com:8080",
            "https://sub.example.com",
            "http://sub.example.com",
            "https://example.com/path/to/resource",
            "http://example.com/path/to/resource",
            "https://example.com/path?query=param",
            "http://example.com/path?query=param",
            "https://example.com/path#fragment",
            "http://example.com/path#fragment"
        )

        @Suppress("MaxLineLength")
        @JvmStatic
        fun invalidUrls() = listOf(
            "ftp://example.com",
            "shttp://example.com",
            "httpz://example.com",
            "http://",
            "http://-asdf.com",
            "http://asdf-.com", "http://asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf.com",
            "http://asdf.c",
            "mailto:asdf@lckj.com"
        )
    }
}
