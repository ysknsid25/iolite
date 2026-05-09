package datetime

import iolite.IoliteException
import iolite.datetime.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DateTest {
    @Test
    fun validDatesShouldParseSuccessfully() {
        for (date in validDates) {
            assertEquals(date, Date(date).parse(), "Failed for date='$date'")
        }
    }

    @Test
    fun invalidDatesShouldThrowExceptions() {
        for (date in invalidDates) {
            assertFailsWith<IoliteException>("Expected fail for date='$date'") {
                Date(date).parse()
            }
        }
    }

    @Test
    fun safeParseShouldReturnSuccessForValidInputs() {
        for (date in validDates) {
            val result = Date(date).safeParse()
            assertTrue(result.isSuccess, "Expected success for date='$date'")
            assertEquals(date, result.getOrThrow())
        }
    }

    @Test
    fun safeParseShouldReturnFailureForInvalidInputs() {
        for (date in invalidDates) {
            val result = Date(date).safeParse()
            assertTrue(result.isFailure, "Expected failure for date='$date'")
            assertFailsWith<IoliteException> { result.getOrThrow() }
        }
    }

    @Test
    fun toStringShouldRenderClassNameAndValue() {
        assertEquals("Date(2022-01-31)", Date("2022-01-31").toString())
    }

    companion object {
        private val validDates = listOf(
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

        private val invalidDates = listOf(
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
