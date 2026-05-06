package personal

import iolite.personal.Email
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class EmailTest {
    @Test
    fun `should create Email instance for valid email`() {
        for (validEmail in validEmails) {
            val email = Email(validEmail)
            assertEquals(
                validEmail.trim().lowercase(),
                email.parse(),
                "Failed for validEmail='$validEmail'"
            )
        }
    }

    @Test
    fun `should throw IllegalArgumentException for invalid email`() {
        for (invalidEmail in invalidEmails) {
            val exception = assertFailsWith<IllegalArgumentException> { Email(invalidEmail).parse() }
            assertEquals("Invalid email address: $invalidEmail", exception.message)
        }
    }

    @Test
    fun `safeParse should return success for valid inputs`() {
        for (validEmail in validEmails) {
            val result = Email(validEmail).safeParse()
            assertTrue(result.isSuccess, "Expected success for validEmail='$validEmail'")
            assertEquals(validEmail.trim().lowercase(), result.getOrThrow())
        }
    }

    @Test
    fun `safeParse should return failure for invalid inputs`() {
        for (invalidEmail in invalidEmails) {
            val result = Email(invalidEmail).safeParse()
            assertTrue(result.isFailure, "Expected failure for invalidEmail='$invalidEmail'")
            assertFailsWith<IllegalArgumentException> { result.getOrThrow() }
        }
    }

    companion object {
        private val validEmails = listOf(
            "email@domain.com",
            "firstname.lastname@domain.com",
            "email@subdomain.domain.com",
            "firstname+lastname@domain.com",
            "1234567890@domain.com",
            "email@domain-one.com",
            "_______@domain.com",
            "email@domain.name",
            "email@domain.co.jp",
            "firstname-lastname@domain.com",
            "very.common@example.com",
            "disposable.style.email.with+symbol@example.com",
            "other.email-with-hyphen@example.com",
            "fully-qualified-domain@example.com",
            "user.name+tag+sorting@example.com",
            "x@example.com",
            "mojojojo@asdf.example.com",
            "example-indeed@strange-example.com",
            "example@s.example",
            "user-@example.org",
            "user@my-example.com",
            "a@b.cd",
            "work+user@mail.com",
            "tom@test.te-st.com",
            "something@subdomain.domain-with-hyphens.tld",
            "common'name@domain.com",
            "francois@etu.inp-n7.fr",
        )

        private val invalidEmails = listOf(
            "francois@@etu.inp-n7.fr",
            "\"email\"@domain.com",
            "\"john..doe\"@example.org",
            "a,b@domain.com",
            "plainaddress",
            "@domain.com",
            "email.domain.com",
            "email@domain@domain.com",
            ".email@domain.com",
            "email.@domain.com",
            "email..email@domain.com",
            "あいうえお@domain.com",
            "email@domain",
            "email@-domain.com",
            "email@111.222.333.44444",
            "email@domain..com",
            "Abc.example.com",
            "A@b@c@example.com",
            "colin..hacks@domain.com",
            "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",
            "just\"not\"right@example.com",
            "this is\"not\\allowed@example.com",
            "this\\ still\\\"not\\\\allowed@example.com",
            "invalid@-start.com",
            "invalid@end.com-",
            "a.b@c.d",
            "invalid@[1.1.1.-1]",
            "invalid@[68.185.127.196.55]",
            "temp@[192.168.1]",
            "temp@[9.18.122.]",
            "double..point@test.com",
            "asdad@test..com",
            "asdad@hghg...sd...au",
            "asdad@hghg........au",
            "invalid@[256.2.2.48]",
            "invalid@[999.465.265.1]",
            "test@.com",
            "aaaaaaaaaaaaaaalongemailthatcausesregexDoSvulnerability@test.c",
        )
    }
}
