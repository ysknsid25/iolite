package network

import iolite.network.Cidr
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CidrTest {
    @ParameterizedTest
    @MethodSource("validCidrs")
    fun `valid CIDRs should parse successfully`(input: String) {
        assertEquals(input, Cidr(input).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidCidrs")
    fun `invalid CIDRs should throw exceptions`(input: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { Cidr(input).parse() }
    }

    @ParameterizedTest
    @MethodSource("validCidrs")
    fun `safeParse should return success for valid inputs`(input: String) {
        val result = Cidr(input).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(input, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidCidrs")
    fun `safeParse should return failure for invalid inputs`(input: String) {
        val result = Cidr(input).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    @Test
    fun `isV4() should return valid result for IPv4 CIDRs`() {
        assertTrue(Cidr("192.168.0.0/24").isV4())
    }

    @Test
    fun `isV4() should return valid result for IPv6 CIDRs`() {
        assertFalse(Cidr("2001:0db8:85a3:0000:0000:8a2e:0370:7334/128").isV4())
    }

    @Test
    fun `isV6() should return valid result for IPv6 CIDRs`() {
        assertTrue(Cidr("2001:0db8:85a3:0000:0000:8a2e:0370:7334/128").isV6())
    }

    @Test
    fun `isV6() should return valid result for IPv4 CIDRs`() {
        assertFalse(Cidr("192.168.0.0/24").isV6())
    }

    companion object {
        @JvmStatic
        fun validCidrs() = listOf(
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

        @Suppress("MaxLineLength")
        @JvmStatic
        fun invalidCidrs() = listOf(
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
