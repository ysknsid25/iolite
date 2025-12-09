package network

import iolite.network.IpV6
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class IpV6Test {
    @ParameterizedTest
    @MethodSource("validIpAddresses")
    fun `valid IP Addresses should parse successfully`(ipAddress: String) {
        assertEquals(ipAddress, IpV6(ipAddress).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidIpAddresses")
    fun `invalid IP Addresses should throw exceptions`(ipAddress: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { IpV6(ipAddress).parse() }
    }

    @ParameterizedTest
    @MethodSource("validIpAddresses")
    fun `safeParse should return success for valid inputs`(ipAddress: String) {
        val result = IpV6(ipAddress).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(ipAddress, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidIpAddresses")
    fun `safeParse should return failure for invalid inputs`(ipAddress: String) {
        val result = IpV6(ipAddress).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validIpAddresses() = listOf(
            "1e5e:e6c8:daac:514b:114b:e360:d8c0:682c",
            "9d4:c956:420f:5788:4339:9b3b:2418:75c3",
            "a6ea::2454:a5ce:94.105.123.75",
            "474f:4c83::4e40:a47:ff95:0cda",
            "d329:0:25b4:db47:a9d1:0:4926:0000",
            "e48:10fb:1499:3e28:e4b6:dea5:4692:912c",
            "::1",
            "2001:db8::",
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334",
            "2001:db8::192.168.0.1",
            "::ffff:192.168.0.1",
            "::ffff:c000:0280",
            "64:ff9b::192.168.0.1"
        )

        @Suppress("MaxLineLength")
        @JvmStatic
        fun invalidIpAddresses() = listOf(
            "d329:1be4:25b4:db47:a9d1:dc71:4926:992c:14af",
            "d5e7:7214:2b78::3906:85e6:53cc:709:32ba",
            "8f69::c757:395e:976e::3441",
            "54cb::473f:d516:0.255.256.22",
            "54cb::473f:d516:192.168.1",
            "114.71.82.94",
            "not an ip",
            "g123::1234:5678"
        )
    }
}
