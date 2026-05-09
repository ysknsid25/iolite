package network

import iolite.IoliteException
import iolite.network.IpV4
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IpV4Test {
    @Test
    fun validIPAddressesShouldParseSuccessfully() {
        for (ipAddress in validIpAddresses) {
            assertEquals(ipAddress, IpV4(ipAddress).parse(), "Failed for ipAddress='$ipAddress'")
        }
    }

    @Test
    fun invalidIPAddressesShouldThrowExceptions() {
        for (ipAddress in invalidIpAddresses) {
            assertFailsWith<IoliteException>("Expected fail for ipAddress='$ipAddress'") {
                IpV4(ipAddress).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (ipAddress in validIpAddresses) {
            val result = IpV4(ipAddress).safeParse()
            assertTrue(result.isSuccess, "Expected success for ipAddress='$ipAddress'")
            assertEquals(ipAddress, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (ipAddress in invalidIpAddresses) {
            val result = IpV4(ipAddress).safeParse()
            assertTrue(result.isFailure, "Expected failure for ipAddress='$ipAddress'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    companion object {
        private val validIpAddresses = listOf(
            "114.71.82.94",
            "0.0.0.0",
            "37.85.236.115",
            "192.168.0.1",
            "255.255.255.255",
            "1.2.3.4",
        )

        @Suppress("MaxLineLength")
        private val invalidIpAddresses = listOf(
            "256.0.4.4",
            "-1.0.555.4",
            "0.0.0.0.0",
            "1.1.1",
            "not an ip",
            "1e5e:e6c8:daac:514b:114b:e360:d8c0:682c",
            "a6ea::2454:a5ce:94.105.123.75",
            "1.2.3",
            "1.2.3.4.5",
            "1.2.3.256",
        )
    }
}
