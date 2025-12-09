package datetime

import iolite.datetime.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class DateTimeTest {
    @ParameterizedTest
    @MethodSource("validDateTimes")
    fun `valid datetimes should parse successfully`(datetimes: String) {
        assertEquals(datetimes, DateTime(datetimes).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDateTimes")
    fun `invalid datetimes should throw exceptions`(datetimes: String) {
        assertThrows<IllegalArgumentException> { DateTime(datetimes).parse() }
    }

    @ParameterizedTest
    @MethodSource("validDateTimesNoMS")
    fun `valid datetimes noms should parse successfully`(datetimes: String) {
        assertEquals(datetimes, DateTime(datetimes, 0).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDateTimesNoMS")
    fun `invalid datetimes noms should throw exceptions`(datetimes: String) {
        assertThrows<IllegalArgumentException> { DateTime(datetimes, 0).parse() }
    }

    @ParameterizedTest
    @MethodSource("validDateTimes3Ms")
    fun `valid datetimes 3ms should parse successfully`(datetimes: String) {
        assertEquals(datetimes, DateTime(datetimes, 3).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDateTimes3Ms")
    fun `invalid datetimes 3ms should throw exceptions`(datetimes: String) {
        assertThrows<IllegalArgumentException> { DateTime(datetimes, 3).parse() }
    }

    @ParameterizedTest
    @MethodSource("validDateTimesOffset")
    fun `valid datetimes with offset should parse successfully`(datetimes: String) {
        assertEquals(datetimes, DateTime(datetimes, offset = true).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDateTimesOffset")
    fun `invalid datetimes with offset should throw exceptions`(datetimes: String) {
        assertThrows<IllegalArgumentException> { DateTime(datetimes, offset = true).parse() }
    }

    @ParameterizedTest
    @MethodSource("validDateTimesOffsetNoMS")
    fun `valid datetimes with offset and no ms should parse successfully`(datetimes: String) {
        assertEquals(datetimes, DateTime(datetimes, 0, offset = true).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDateTimesOffsetNoMS")
    fun `invalid datetimes with offset and no ms should throw exceptions`(datetimes: String) {
        assertThrows<IllegalArgumentException> { DateTime(datetimes, 0, offset = true).parse() }
    }

    @ParameterizedTest
    @MethodSource("validDateTimesOffset4Ms")
    fun `valid datetimes with offset and 4ms should parse successfully`(datetimes: String) {
        assertEquals(datetimes, DateTime(datetimes, 4, offset = true).parse())
    }

    @ParameterizedTest
    @MethodSource("invalidDateTimesOffset4Ms")
    fun `invalid datetimes with offset and 4ms should throw exceptions`(datetimes: String) {
        assertThrows<IllegalArgumentException> { DateTime(datetimes, 4, offset = true).parse() }
    }

    companion object {
        @JvmStatic
        fun validDateTimes() = listOf(
            "1970-01-01T00:00:00.000Z",
            "2022-10-13T09:52:31.816Z",
            "2022-10-13T09:52:31.8162314Z",
            "1970-01-01T00:00:00Z",
            "2022-10-13T09:52:31Z",
            "2022-10-13T09:52Z",
        )

        @JvmStatic
        fun invalidDateTimes() = listOf(
            "",
            "foo",
            "2020-10-14",
            "T18:45:12.123",
            "2020-10-14T17:42:29+00:00",
            "2020-10-14T17:42.123+00:00",
        )

        @JvmStatic
        fun validDateTimesNoMS() = listOf(
            "1970-01-01T00:00:00Z",
            "2022-10-13T09:52:31Z",
            "2022-10-13T09:52Z",
        )

        @JvmStatic
        fun invalidDateTimesNoMS() = listOf(
            "tuna",
            "1970-01-01T00:00:00.000Z",
            "1970-01-01T00:00:00.Z",
            "2022-10-13T09:52:31.816Z",
        )

        @JvmStatic
        fun validDateTimes3Ms() = listOf(
            "1970-01-01T00:00:00.000Z",
            "2022-10-13T09:52:31.123Z",
        )

        @JvmStatic
        fun invalidDateTimes3Ms() = listOf(
            "tuna",
            "1970-01-01T00:00:00.1Z",
            "1970-01-01T00:00:00.12Z",
            "2022-10-13T09:52:31Z",
            "2022-10-13T09:52Z",
        )

        @JvmStatic
        fun validDateTimesOffset() = listOf(
            "1970-01-01T00:00:00.000Z",
            "2022-10-13T09:52:31.816234134Z",
            "1970-01-01T00:00:00Z",
            "2022-10-13T09:52:31.4Z",
            "2020-10-14T17:42:29+00:00",
            "2020-10-14T17:42:29+03:15",
            "2020-10-14T17:42:29+0315",
            "2020-10-14T17:42+0315",
        )

        @JvmStatic
        fun invalidDateTimesOffset() = listOf(
            "2020-10-14T17:42:29+03",
            "tuna",
            "2022-10-13T09:52:31.Z",
        )

        @JvmStatic
        fun validDateTimesOffsetNoMS() = listOf(
            "1970-01-01T00:00:00Z",
            "2022-10-13T09:52:31Z",
            "2020-10-14T17:42:29+00:00",
            "2020-10-14T17:42:29+0000",
            "2020-10-14T17:42+0000",
        )

        @JvmStatic
        fun invalidDateTimesOffsetNoMS() = listOf(
            "2020-10-14T17:42:29+00",
            "tuna",
            "1970-01-01T00:00:00.000Z",
            "1970-01-01T00:00:00.Z",
            "2022-10-13T09:52:31.816Z",
            "2020-10-14T17:42:29.124+00:00",
        )

        @JvmStatic
        fun validDateTimesOffset4Ms() = listOf(
            "1970-01-01T00:00:00.1234Z",
            "2020-10-14T17:42:29.1234+00:00",
            "2020-10-14T17:42:29.1234+0000",
        )

        @JvmStatic
        fun invalidDateTimesOffset4Ms() = listOf(
            "2020-10-14T17:42:29.1234+00",
            "tuna",
            "1970-01-01T00:00:00.123Z",
            "2020-10-14T17:42:29.124+00:00",
            "2020-10-14T17:42+00:00",
        )
    }
}
