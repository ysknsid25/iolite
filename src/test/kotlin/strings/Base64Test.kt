package strings

import iolite.strings.Base64
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class Base64Test {
    @ParameterizedTest
    @MethodSource("validBase64Strings")
    fun `valid URLs should parse successfully`(base64: String) {
        assertEquals(base64, Base64(base64).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidBase64Strings")
    fun `invalid URLs should throw exceptions`(base64: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { Base64(base64).parse() }
    }

    @ParameterizedTest
    @MethodSource("validBase64Strings")
    fun `safeParse should return success for valid inputs`(base64: String) {
        val result = Base64(base64).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(base64, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidBase64Strings")
    fun `safeParse should return failure for invalid inputs`(base64: String) {
        val result = Base64(base64).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @Suppress("MaxLineLength")
        @JvmStatic
        fun validBase64Strings() = listOf(
            "", // ""
            "Zg==", // "f"
            "Zm8=", // "fo"
            "Zm9v", // "foo"
            "Zm9vYg==", // "foob"
            "Zm9vYmE=", // "fooba"
            "Zm9vYmFy", // "foobar"

            // Other custom tests
            "dmFsaWJvdA==", // "valibot"
            "SGVsbG8sIEkgYW0gVmFsaWJvdCBhbmQgSSB3b3VsZCBsaWtlIHRvIGhlbHAgeW91IHZhbGlkYXRlIGRhdGEgZWFzaWx5IHVzaW5nIGEgc2NoZW1hLg==", // "Hello, I am Valibot and I would like to help you validate data easily using a schema."
            "8J+Mrg==", // "🌮"
        )

        @Suppress("MaxLineLength")
        @JvmStatic
        fun invalidBase64Strings() = listOf(
            "foo`", // `
            "foo~", // ~
            "foo!", // !
            "foo@", // @
            "foo#", // #
            "foo$", // $
            "foo%", // %
            "foo^", // ^
            "foo&", // &
            "foo*", // *
            "foo(", // (
            "foo)", // )
            "foo-", // -
            "foo_", // _
            "foo[", // [
            "foo]", // ]
            "foo{", // {
            "foo}", // }
            "foo\\", // \
            "foo|", // |
            "foo;", // ;
            "foo:", // :
            "foo'", // '
            "foo\"", // "
            "foo,", // ,
            "foo.", // .
            "foo<", // <
            "foo>", // >
            "foo?", // ?
            "dmFsaWJvdA", // == missing
            "dmFsaWJvdA=", // = missing
            "dmFsaWJvdA===", // = extra
            "Zm9vYmE", // = missing
            "Zm9vYmE==", // = extra
        )
    }
}
