package datetime

import iolite.datetime.Date
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class DateTest {
    @ParameterizedTest
    @MethodSource("validDates")
    fun `valid dates should parse successfully`(date: String) {
        assertEquals(date, Date(date).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDates")
    fun `invalid dates should throw exceptions`(date: String) {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { Date(date).parse() }
    }

    @ParameterizedTest
    @MethodSource("validDates")
    fun `safeParse should return success for valid inputs`(date: String) {
        val result = Date(date).safeParse()
        assertTrue(result.isSuccess)
        assertEquals(date, result.getOrThrow())
    }

    @ParameterizedTest
    @MethodSource("invalidDates")
    fun `safeParse should return failure for invalid inputs`(date: String) {
        val result = Date(date).safeParse()
        assertTrue(result.isFailure)
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { result.getOrThrow() }
    }

    companion object {
        @JvmStatic
        fun validDates() = listOf(
            "1970-01-01",
            "2022-01-31",
            "2022-03-31",
            "2022-04-30",
            "2022-05-31",
            "2022-06-30",
            "2022-07-31",
            "2022-08-31",
            "2022-09-30",
            "2022-10-31",
            "2022-11-30",
            "2022-12-31",
            "2000-02-29",
            "2400-02-29",
        )

        @JvmStatic
        fun invalidDates() = listOf(
            "2022-02-29",
            "2100-02-29",
            "2200-02-29",
            "2300-02-29",
            "2500-02-29",
            "",
            "foo",
            "200-01-01",
            "20000-01-01",
            "2000-0-01",
            "2000-011-01",
            "2000-01-0",
            "2000-01-011",
            "2000/01/01",
            "01-01-2022",
            "01/01/2022",
            "2000-01-01 00:00:00Z",
            "2020-10-14T17:42:29+00:00",
            "2020-10-14T17:42:29Z",
            "2020-10-14T17:42:29",
            "2020-10-14T17:42:29.123Z",
            "2000-00-12",
            "2000-12-00",
            "2000-01-32",
            "2000-13-01",
            "2000-21-01",
            "2000-02-30",
            "2000-02-31",
            "2000-04-31",
            "2000-06-31",
            "2000-09-31",
            "2000-11-31",
        )
    }
}
