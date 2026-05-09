package network

import iolite.IoliteException
import iolite.network.IpV6
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IpV6Test {
    @Test
    fun validIPAddressesShouldParseSuccessfully() {
        for (ipAddress in validIpAddresses) {
            assertEquals(ipAddress, IpV6(ipAddress).parse(), "Failed for ipAddress='$ipAddress'")
        }
    }

    @Test
    fun invalidIPAddressesShouldThrowExceptions() {
        for (ipAddress in invalidIpAddresses) {
            assertFailsWith<IoliteException>("Expected fail for ipAddress='$ipAddress'") {
                IpV6(ipAddress).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (ipAddress in validIpAddresses) {
            val result = IpV6(ipAddress).safeParse()
            assertTrue(result.isSuccess, "Expected success for ipAddress='$ipAddress'")
            assertEquals(ipAddress, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (ipAddress in invalidIpAddresses) {
            val result = IpV6(ipAddress).safeParse()
            assertTrue(result.isFailure, "Expected failure for ipAddress='$ipAddress'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    @Test
    fun toStringShouldRenderClassNameAndValue() {
        assertEquals("IpV6(::1)", IpV6("::1").toString())
    }

    companion object {
        private val validIpAddresses = listOf(
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
            "64:ff9b::192.168.0.1",
        )

        @Suppress("MaxLineLength")
        private val invalidIpAddresses = listOf(
            "d329:1be4:25b4:db47:a9d1:dc71:4926:992c:14af",
            "d5e7:7214:2b78::3906:85e6:53cc:709:32ba",
            "8f69::c757:395e:976e::3441",
            "54cb::473f:d516:0.255.256.22",
            "54cb::473f:d516:192.168.1",
            "114.71.82.94",
            "not an ip",
            "g123::1234:5678",
        )
    }
}
