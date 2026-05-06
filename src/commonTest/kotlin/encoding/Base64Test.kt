package encoding

import iolite.encoding.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class Base64Test {
    @Test
    fun `valid Base64 should parse successfully`() {
        for (base64 in validBase64Strings) {
            assertEquals(base64, Base64(base64).parse(), "Failed for base64='$base64'")
        }
    }

    @Test
    fun `invalid Base64 should throw exceptions`() {
        for (base64 in invalidBase64Strings) {
            assertFailsWith<IllegalArgumentException>("Expected fail for base64='$base64'") {
                Base64(base64).parse()
            }
        }
    }

    @Test
    fun `safeParse should return success for valid inputs`() {
        for (base64 in validBase64Strings) {
            val result = Base64(base64).safeParse()
            assertTrue(result.isSuccess, "Expected success for base64='$base64'")
            assertEquals(base64, result.getOrThrow())
        }
    }

    @Test
    fun `safeParse should return failure for invalid inputs`() {
        for (base64 in invalidBase64Strings) {
            val result = Base64(base64).safeParse()
            assertTrue(result.isFailure, "Expected failure for base64='$base64'")
            assertFailsWith<IllegalArgumentException> { result.getOrThrow() }
        }
    }

    companion object {
        @Suppress("MaxLineLength")
        private val validBase64Strings = listOf(
            "",
            "Zg==",
            "Zm8=",
            "Zm9v",
            "Zm9vYg==",
            "Zm9vYmE=",
            "Zm9vYmFy",
            "dmFsaWJvdA==",
            "SGVsbG8sIEkgYW0gVmFsaWJvdCBhbmQgSSB3b3VsZCBsaWtlIHRvIGhlbHAgeW91IHZhbGlkYXRlIGRhdGEgZWFzaWx5IHVzaW5nIGEgc2NoZW1hLg==",
            "8J+Mrg==",
        )

        private val invalidBase64Strings = listOf(
            "foo`",
            "foo~",
            "foo!",
            "foo@",
            "foo#",
            "foo$",
            "foo%",
            "foo^",
            "foo&",
            "foo*",
            "foo(",
            "foo)",
            "foo-",
            "foo_",
            "foo[",
            "foo]",
            "foo{",
            "foo}",
            "foo\\",
            "foo|",
            "foo;",
            "foo:",
            "foo'",
            "foo\"",
            "foo,",
            "foo.",
            "foo<",
            "foo>",
            "foo?",
            "dmFsaWJvdA",
            "dmFsaWJvdA=",
            "dmFsaWJvdA===",
            "Zm9vYmE",
            "Zm9vYmE==",
        )
    }
}
