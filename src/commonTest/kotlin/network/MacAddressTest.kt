package network

import iolite.IoliteException
import iolite.network.MacAddress
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MacAddressTest {
    @Test
    fun validMACAddressesShouldParseSuccessfully() {
        for (macAddress in validMacAddresses) {
            assertEquals(macAddress, MacAddress(macAddress).parse(), "Failed for macAddress='$macAddress'")
        }
    }

    @Test
    fun invalidMACAddressesShouldThrowExceptions() {
        for (macAddress in invalidMacAddresses) {
            assertFailsWith<IoliteException>("Expected fail for macAddress='$macAddress'") {
                MacAddress(macAddress).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (macAddress in validMacAddresses) {
            val result = MacAddress(macAddress).safeParse()
            assertTrue(result.isSuccess, "Expected success for macAddress='$macAddress'")
            assertEquals(macAddress, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (macAddress in invalidMacAddresses) {
            val result = MacAddress(macAddress).safeParse()
            assertTrue(result.isFailure, "Expected failure for macAddress='$macAddress'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    @Test
    fun toStringShouldRenderClassNameAndValue() {
        assertEquals(
            "MacAddress(00:1A:2B:3C:4D:5E)",
            MacAddress("00:1A:2B:3C:4D:5E").toString()
        )
    }

    companion object {
        @Suppress("LongMethod")
        private val validMacAddresses = listOf(
            "00:1A:2B:3C:4D:5E",
            "AA:BB:CC:DD:EE:FF",
            "00-1A-2B-3C-4D-5E",
            "aa-bb-cc-dd-ee-ff",
            "001A.2B3C.4D5E",
            "a1b2.c3d4.e5f6",
            "b6:05:20:67:f9:58",
            "B6:05:20:67:F9:58",
            "b6-05-20-67-f9-58",
            "B6-05-20-67-F9-58",
            "b605.2067.f958",
            "B605.2067.F958",
            "00:00:00:00:00:00",
            "99:99:99:99:99:99",
            "aa:aa:aa:aa:aa:aa",
            "AA:AA:AA:AA:AA:AA",
            "ff:ff:ff:ff:ff:ff",
            "FF:FF:FF:FF:FF:FF",
            "00-00-00-00-00-00",
            "99-99-99-99-99-99",
            "aa-aa-aa-aa-aa-aa",
            "AA-AA-AA-AA-AA-AA",
            "ff-ff-ff-ff-ff-ff",
            "FF-FF-FF-FF-FF-FF",
            "0000.0000.0000",
            "9999.9999.9999",
            "aaaa.aaaa.aaaa",
            "AAAA.AAAA.AAAA",
            "ffff.ffff.ffff",
            "FFFF.FFFF.FFFF",
            "00:25:96:FF:FE:12:34:56",
            "00-1A-2B-3C-4D-5E-6F-70",
            "0025.96FF.FE12.3456",
            "0025:96FF:FE12:3456",
            "00:00:00:00:00:00:00:00",
            "99:99:99:99:99:99:99:99",
            "aa:aa:aa:aa:aa:aa:aa:aa",
            "AA:AA:AA:AA:AA:AA:AA:AA",
            "ff:ff:ff:ff:ff:ff:ff:ff",
            "FF:FF:FF:FF:FF:FF:FF:FF",
            "00-00-00-00-00-00-00-00",
            "99-99-99-99-99-99-99-99",
            "aa-aa-aa-aa-aa-aa-aa-aa",
            "AA-AA-AA-AA-AA-AA-AA-AA",
            "ff-ff-ff-ff-ff-ff-ff-ff",
            "FF-FF-FF-FF-FF-FF-FF-FF",
            "0000.0000.0000.0000",
            "9999.9999.9999.9999",
            "aaaa.aaaa.aaaa.aaaa",
            "AAAA.AAAA.AAAA.AAAA",
            "ffff.ffff.ffff.ffff",
            "FFFF.FFFF.FFFF.FFFF",
            "0000:0000:0000:0000",
            "9999:9999:9999:9999",
            "aaaa:aaaa:aaaa:aaaa",
            "AAAA:AAAA:AAAA:AAAA",
            "ffff:ffff:ffff:ffff",
            "FFFF:FFFF:FFFF:FFFF",
        )

        @Suppress("LongMethod")
        private val invalidMacAddresses = listOf(
            "00:1A-2B:3C-4D:5E",
            "00:1A:2B:3C:4D:5G",
            "00:1A:2B:3C:4D",
            "001A2B3C4D5E",
            "0:1A:2B:3C:4D:5E",
            "00:1A:2B:3C:4D:",
            "00:1A:2B: 3C:4D:5E",
            "001A-2B3C-4D5E",
            "GG:HH:II:JJ:KK:LL",
            "00:1G:2B:3C:4D:5E",
            "00:1A:2B:3C:4D",
            "00:1A:2B:3C:4D:5E:6F",
            "00:1A:2B:3C:4D:5E:6F:70:80",
            "00-1G-2B-3C-4D-5E",
            "00-1A-2B-3C-4D",
            "00-1A-2B-3C-4D-5E-6F",
            "00-1A-2B-3C-4D-5E-6F-70-80",
            "b605-2067-f958",
            "00_1A_2B_3C_4D_5E",
            "001A2B3C4D5E6F",
            "ZZ:ZZ:ZZ:ZZ:ZZ:ZZ",
            "00:1A:2B:3C:4D:5E:6F:70:80:90:AB",
            "001122334455",
            "00:1A:2B:3C:4D:5E:6F:70:ZZ",
            "GHIJ:KLNM:OPQR",
            "00:00:00:00:00:00:00",
            "00-00-00-00-00-00-00",
            "0:0:0:0:0:0",
            "000:000:000:000:000:000",
            "00:00:00:00:00",
            "0-0-0-0-0-0",
            "000-000-000-000-000-000",
            "00-00-00-00-00",
            "000.000.000",
            "00000.00000.00000",
            "-10:-10:-10:-10:-10:-10",
            "100:100:100:100:100:100",
            "gg:gg:gg:gg:gg:gg",
            "GG:GG:GG:GG:GG:GG",
            "zz:zz:zz:zz:zz:zz",
            "-10--10--10--10--10--10",
            "100-100-100-100-100-100",
            "gg-gg-gg-gg-gg-gg",
            "GG-GG-GG-GG-GG-GG",
            "zz-zz-zz-zz-zz-zz",
            "ZZ-ZZ-ZZ-ZZ-ZZ-ZZ",
            "-1000.-1000.-1000",
            "10000.10000.10000",
            "gggg.gggg.gggg",
            "GGGG.GGGG.GGGG",
            "zzzz.zzzz.zzzz",
            "ZZZZ.ZZZZ.ZZZZ",
            "0:0:0:0:0:0:0:0",
            "000:000:000:000:000:000:000:000",
            "00:00:00:00:00:00:00:00:00",
            "0-0-0-0-0-0-0-0",
            "000-000-000-000-000-000-000-000",
            "00-00-00-00-00-00-00-00-00",
            "000.000.000.000",
            "00000.00000.00000.00000",
            "0000.0000.0000.0000.0000",
            "000:000:000:000",
            "00000:00000:00000:00000",
            "0000:0000:0000",
            "0000:0000:0000:0000:0000",
            "-10:-10:-10:-10:-10:-10:-10:-10",
            "10000:10000:10000:10000:10000:10000:10000:10000",
            "zzzz:zzzz:zzzz:zzzz:zzzz:zzzz:zzzz:zzzz",
            "ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ:ZZZZ",
            "gggg:gggg:gggg:gggg:gggg:gggg:gggg:gggg",
            "GGGG:GGGG:GGGG:GGGG:GGGG:GGGG:GGGG:GGGG",
            "-10--10--10--10--10--10--10--10",
            "10000-10000-10000-10000-10000-10000-10000-10000",
            "zzzz-zzzz-zzzz-zzzz-zzzz-zzzz-zzzz-zzzz",
            "ZZZZ-ZZZZ-ZZZZ-ZZZZ-ZZZZ-ZZZZ-ZZZZ-ZZZZ",
            "gggg-gggg-gggg-gggg-gggg-gggg-gggg-gggg",
            "GGGG-GGGG-GGGG-GGGG-GGGG-GGGG-GGGG-GGGG",
            "-1000.-1000.-1000.-1000",
            "10000.10000.10000.10000",
            "zzzz.zzzz.zzzz.zzzz",
            "ZZZZ.ZZZZ.ZZZZ.ZZZZ",
            "gggg.gggg.gggg.gggg",
            "GGGG.GGGG.GGGG.GGGG",
            "-1000:-1000:-1000:-1000",
            "10000:10000:10000:10000",
            "zzzz:zzzz:zzzz:zzzz",
            "ZZZZ:ZZZZ:ZZZZ:ZZZZ",
            "gggg:gggg:gggg:gggg",
            "GGGG:GGGG:GGGG:GGGG",
        )
    }
}
