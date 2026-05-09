package datetime

import iolite.datetime.DateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Suppress("ArgumentListWrapping")
class DateTimeTest {
    @Test
    fun validDatetimesShouldParseSuccessfully() {
        for (input in validDateTimes) {
            assertEquals(input, DateTime(input).parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidDatetimesShouldThrowExceptions() {
        for (input in invalidDateTimes) {
            assertFailsWith<IllegalArgumentException>("Expected fail for input='$input'") {
                DateTime(input).parse()
            }
        }
    }

    @Test
    fun validDatetimesNomsShouldParseSuccessfully() {
        for (input in validDateTimesNoMS) {
            assertEquals(input, DateTime(input, 0).parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidDatetimesNomsShouldThrowExceptions() {
        for (input in invalidDateTimesNoMS) {
            assertFailsWith<IllegalArgumentException>("Expected fail for input='$input'") {
                DateTime(input, 0).parse()
            }
        }
    }

    @Test
    fun validDatetimes3msShouldParseSuccessfully() {
        for (input in validDateTimes3Ms) {
            assertEquals(input, DateTime(input, 3).parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidDatetimes3msShouldThrowExceptions() {
        for (input in invalidDateTimes3Ms) {
            assertFailsWith<IllegalArgumentException>("Expected fail for input='$input'") {
                DateTime(input, 3).parse()
            }
        }
    }

    @Test
    fun validDatetimesWithOffsetShouldParseSuccessfully() {
        for (input in validDateTimesOffset) {
            assertEquals(input, DateTime(input, offset = true).parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidDatetimesWithOffsetShouldThrowExceptions() {
        for (input in invalidDateTimesOffset) {
            assertFailsWith<IllegalArgumentException>("Expected fail for input='$input'") {
                DateTime(input, offset = true).parse()
            }
        }
    }

    @Test
    fun validDatetimesWithOffsetAndNoMsShouldParseSuccessfully() {
        for (input in validDateTimesOffsetNoMS) {
            assertEquals(input, DateTime(input, 0, offset = true).parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidDatetimesWithOffsetAndNoMsShouldThrowExceptions() {
        for (input in invalidDateTimesOffsetNoMS) {
            assertFailsWith<IllegalArgumentException>("Expected fail for input='$input'") {
                DateTime(input, 0, offset = true).parse()
            }
        }
    }

    @Test
    fun validDatetimesWithOffsetAnd4msShouldParseSuccessfully() {
        for (input in validDateTimesOffset4Ms) {
            assertEquals(input, DateTime(input, 4, offset = true).parse(), "Failed for input='$input'")
        }
    }

    @Test
    fun invalidDatetimesWithOffsetAnd4msShouldThrowExceptions() {
        for (input in invalidDateTimesOffset4Ms) {
            assertFailsWith<IllegalArgumentException>("Expected fail for input='$input'") {
                DateTime(input, 4, offset = true).parse()
            }
        }
    }

    companion object {
        private val validDateTimes = listOf(
            "1970-01-01T00:00:00.000Z",
            "2022-10-13T09:52:31.816Z",
            "2022-10-13T09:52:31.8162314Z",
            "1970-01-01T00:00:00Z",
            "2022-10-13T09:52:31Z",
            "2022-10-13T09:52Z",
        )

        private val invalidDateTimes = listOf(
            "",
            "foo",
            "2020-10-14",
            "T18:45:12.123",
            "2020-10-14T17:42:29+00:00",
            "2020-10-14T17:42.123+00:00",
        )

        private val validDateTimesNoMS = listOf(
            "1970-01-01T00:00:00Z",
            "2022-10-13T09:52:31Z",
            "2022-10-13T09:52Z",
        )

        private val invalidDateTimesNoMS = listOf(
            "tuna",
            "1970-01-01T00:00:00.000Z",
            "1970-01-01T00:00:00.Z",
            "2022-10-13T09:52:31.816Z",
        )

        private val validDateTimes3Ms = listOf(
            "1970-01-01T00:00:00.000Z",
            "2022-10-13T09:52:31.123Z",
        )

        private val invalidDateTimes3Ms = listOf(
            "tuna",
            "1970-01-01T00:00:00.1Z",
            "1970-01-01T00:00:00.12Z",
            "2022-10-13T09:52:31Z",
            "2022-10-13T09:52Z",
        )

        private val validDateTimesOffset = listOf(
            "1970-01-01T00:00:00.000Z",
            "2022-10-13T09:52:31.816234134Z",
            "1970-01-01T00:00:00Z",
            "2022-10-13T09:52:31.4Z",
            "2020-10-14T17:42:29+00:00",
            "2020-10-14T17:42:29+03:15",
            "2020-10-14T17:42:29+0315",
            "2020-10-14T17:42+0315",
        )

        private val invalidDateTimesOffset = listOf(
            "2020-10-14T17:42:29+03",
            "tuna",
            "2022-10-13T09:52:31.Z",
        )

        private val validDateTimesOffsetNoMS = listOf(
            "1970-01-01T00:00:00Z",
            "2022-10-13T09:52:31Z",
            "2020-10-14T17:42:29+00:00",
            "2020-10-14T17:42:29+0000",
            "2020-10-14T17:42+0000",
        )

        private val invalidDateTimesOffsetNoMS = listOf(
            "2020-10-14T17:42:29+00",
            "tuna",
            "1970-01-01T00:00:00.000Z",
            "1970-01-01T00:00:00.Z",
            "2022-10-13T09:52:31.816Z",
            "2020-10-14T17:42:29.124+00:00",
        )

        private val validDateTimesOffset4Ms = listOf(
            "1970-01-01T00:00:00.1234Z",
            "2020-10-14T17:42:29.1234+00:00",
            "2020-10-14T17:42:29.1234+0000",
        )

        private val invalidDateTimesOffset4Ms = listOf(
            "2020-10-14T17:42:29.1234+00",
            "tuna",
            "1970-01-01T00:00:00.123Z",
            "2020-10-14T17:42:29.124+00:00",
            "2020-10-14T17:42+00:00",
        )
    }
}
