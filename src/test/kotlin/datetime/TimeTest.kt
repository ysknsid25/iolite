package datetime

import iolite.datetime.Time
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class TimeTest {
    @ParameterizedTest
    @MethodSource("validTimes")
    fun `valid times should parse successfully`(time: String) {
        assertEquals(time, Time(time).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidTimes")
    fun `invalid times should throw exceptions`(time: String) {
        assertThrows<IllegalArgumentException> { Time(time).parse() }
    }

    @ParameterizedTest
    @MethodSource("validTimes")
    fun `safeParse should return success for valid inputs`(time: String) {
        val result = Time(time).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(time, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidTimes")
    fun `safeParse should return failure for invalid inputs`(time: String) {
        val result = Time(time).safeParse()
        assertTrue(result.isFailure)
        assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    @ParameterizedTest
    @MethodSource("validTimesPrecision2")
    fun `valid times precision2 should parse successfully`(time: String) {
        assertEquals(time, Time(time, 2).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidTimesPrecision2")
    fun `invalid times precision2 should throw exceptions`(time: String) {
        assertThrows<IllegalArgumentException> { Time(time, 2).parse() }
    }

    companion object {
        @JvmStatic
        fun validTimes() = listOf(
            "00:00:00",
            "23:00:00",
            "00:59:00",
            "00:00:59",
            "23:59:59",
            "09:52:31",
            "23:59:59.9999999",
            "23:59",
        )

        @JvmStatic
        fun invalidTimes() = listOf(
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

        @JvmStatic
        fun validTimesPrecision2() = listOf(
            "00:00:00.00",
            "09:52:31.12",
            "23:59:59.99",
        )

        @JvmStatic
        fun invalidTimesPrecision2() = listOf(
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
