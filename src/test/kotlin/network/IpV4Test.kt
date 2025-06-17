package network

import iolite.network.IpV4
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class IpV4Test {
    @ParameterizedTest
    @MethodSource("validIpAddresses")
    fun `valid IP Addresses should parse successfully`(ipAddress: String) {
        assertEquals(ipAddress, IpV4(ipAddress).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidIpAddresses")
    fun `invalid IP Addresses should throw exceptions`(ipAddress: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { IpV4(ipAddress).parse() }
    }

    @ParameterizedTest
    @MethodSource("validIpAddresses")
    fun `safeParse should return success for valid inputs`(ipAddress: String) {
        val result = IpV4(ipAddress).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(ipAddress, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidIpAddresses")
    fun `safeParse should return failure for invalid inputs`(ipAddress: String) {
        val result = IpV4(ipAddress).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validIpAddresses() = listOf(
            "114.71.82.94",
            "0.0.0.0",
            "37.85.236.115",
            "192.168.0.1",
            "255.255.255.255",
            "1.2.3.4"
        )

        @Suppress("MaxLineLength")
        @JvmStatic
        fun invalidIpAddresses() = listOf(
            "256.0.4.4",
            "-1.0.555.4",
            "0.0.0.0.0",
            "1.1.1",
            "not an ip",
            "1e5e:e6c8:daac:514b:114b:e360:d8c0:682c",
            "a6ea::2454:a5ce:94.105.123.75",
            "1.2.3",
            "1.2.3.4.5",
            "1.2.3.256"
        )
    }
}
