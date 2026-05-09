package network

import iolite.IoliteException
import iolite.network.Cidr
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CidrTest {
    @Test
    fun validCIDRsShouldParseSuccessfully() {
        for (input in validCidrs) {
            assertEquals(input, Cidr(input).parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidCIDRsShouldThrowExceptions() {
        for (input in invalidCidrs) {
            assertFailsWith<IoliteException>("Expected fail for input='$input'") {
                Cidr(input).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (input in validCidrs) {
            val result = Cidr(input).safeParse()
            assertTrue(result.isSuccess, "Expected success for input='$input'")
            assertEquals(input, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (input in invalidCidrs) {
            val result = Cidr(input).safeParse()
            assertTrue(result.isFailure, "Expected failure for input='$input'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    @Test
    fun isV4ShouldReturnValidResultForIPv4CIDRs() {
        assertTrue(Cidr("192.168.0.0/24").isV4())
    }

    @Test
    fun isV4ShouldReturnValidResultForIPv6CIDRs() {
        assertFalse(Cidr("2001:0db8:85a3:0000:0000:8a2e:0370:7334/128").isV4())
    }

    @Test
    fun isV6ShouldReturnValidResultForIPv6CIDRs() {
        assertTrue(Cidr("2001:0db8:85a3:0000:0000:8a2e:0370:7334/128").isV6())
    }

    @Test
    fun isV6ShouldReturnValidResultForIPv4CIDRs() {
        assertFalse(Cidr("192.168.0.0/24").isV6())
    }

    companion object {
        private val validCidrs = listOf(
            "192.168.0.0/24",
            "10.0.0.0/8",
            "203.0.113.0/24",
            "192.0.2.0/24",
            "127.0.0.0/8",
            "172.16.0.0/12",
            "192.168.1.0/24",
            "fc00::/7",
            "fd00::/8",
            "2001:db8::/32",
            "2607:f0d0:1002:51::4/64",
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334/128",
            "2001:0db8:1234:0000::/64",
        )

        private val invalidCidrs = listOf(
            "192.168.1.1/33",
            "10.0.0.1/-1",
            "192.168.1.1/24/24",
            "192.168.1.0/abc",
            "2001:db8::1/129",
            "2001:db8::1/-1",
            "2001:db8::1/64/64",
            "2001:db8::1/abc",
        )
    }
}
