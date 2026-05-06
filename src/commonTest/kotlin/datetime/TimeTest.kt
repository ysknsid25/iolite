package datetime

import iolite.datetime.Time
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TimeTest {
    @Test
    fun `valid times should parse successfully`() {
        for (time in validTimes) {
            assertEquals(time, Time(time).parse(), "Failed for time='$time'")
        }
    }

    @Test
    fun `invalid times should throw exceptions`() {
        for (time in invalidTimes) {
            assertFailsWith<IllegalArgumentException>("Expected fail for time='$time'") {
                Time(time).parse()
            }
        }
    }

    @Test
    fun `safeParse should return success for valid inputs`() {
        for (time in validTimes) {
            val result = Time(time).safeParse()
            assertTrue(result.isSuccess, "Expected success for time='$time'")
            assertEquals(time, result.getOrThrow())
        }
    }

    @Test
    fun `safeParse should return failure for invalid inputs`() {
        for (time in invalidTimes) {
            val result = Time(time).safeParse()
            assertTrue(result.isFailure, "Expected failure for time='$time'")
            assertFailsWith<IllegalArgumentException> { result.getOrThrow() }
        }
    }

    @Test
    fun `valid times precision2 should parse successfully`() {
        for (time in validTimesPrecision2) {
            assertEquals(time, Time(time, 2).parse(), "Failed for time='$time'")
        }
    }

    @Test
    fun `invalid times precision2 should throw exceptions`() {
        for (time in invalidTimesPrecision2) {
            assertFailsWith<IllegalArgumentException>("Expected fail for time='$time'") {
                Time(time, 2).parse()
            }
        }
    }

    companion object {
        private val validTimes = listOf(
            "00:00:00",
            "23:00:00",
            "00:59:00",
            "00:00:59",
            "23:59:59",
            "09:52:31",
            "23:59:59.9999999",
            "23:59",
        )

        private val invalidTimes = listOf(
            "",
            "foo",
            "00:00:00Z",
            "0:00:00",
            "00:0:00",
            "00:00:0",
            "00:00:00.000+00:00",
            "24:00:00",
            "00:60:00",
            "00:00:60",
            "24:60:60",
            "24:60",
        )

        private val validTimesPrecision2 = listOf(
            "00:00:00.00",
            "09:52:31.12",
            "23:59:59.99",
        )

        private val invalidTimesPrecision2 = listOf(
            "",
            "foo",
            "00:00:00",
            "00:00:00.00Z",
            "00:00:00.0",
            "00:00:00.000",
            "00:00:00.00+00:00",
            "23:59",
        )
    }
}
