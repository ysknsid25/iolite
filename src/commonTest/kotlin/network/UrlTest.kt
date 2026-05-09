package network

import iolite.network.Url
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UrlTest {
    @Test
    fun validURLsShouldParseSuccessfully() {
        for (url in validUrls) {
            assertEquals(url, Url(url).parse(), "Failed for url='$url'")
        }
    }

    @Test
    fun invalidURLsShouldThrowExceptions() {
        for (url in invalidUrls) {
            assertFailsWith<IllegalArgumentException>("Expected fail for url='$url'") {
                Url(url).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (url in validUrls) {
            val result = Url(url).safeParse()
            assertTrue(result.isSuccess, "Expected success for url='$url'")
            assertEquals(url, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (url in invalidUrls) {
            val result = Url(url).safeParse()
            assertTrue(result.isFailure, "Expected failure for url='$url'")
            assertFailsWith<IllegalArgumentException> { result.getOrThrow() }
        }
    }

    companion object {
        private val validUrls = listOf(
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
            "http://example.com/path#fragment",
        )

        @Suppress("MaxLineLength")
        private val invalidUrls = listOf(
            "ftp://example.com",
            "shttp://example.com",
            "httpz://example.com",
            "http://",
            "http://-asdf.com",
            "http://asdf-.com",
            "http://asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf.com",
            "http://asdf.c",
            "mailto:asdf@lckj.com",
        )
    }
}
