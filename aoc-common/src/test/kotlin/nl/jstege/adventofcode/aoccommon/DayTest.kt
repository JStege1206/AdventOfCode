package nl.jstege.adventofcode.aoccommon

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.DayTester

/**
 *
 * @author Jelle Stege
 */
class DayTest : DayTester(DayTestImpl()) {
    class DayTestImpl : Day() {
        val FIRST_SLEEP_TIME = 1000L
        val SECOND_SLEEP_TIME = 1500L

        override suspend fun first(input: Sequence<String>): Any {
            Thread.sleep(FIRST_SLEEP_TIME)
            return input.first()
        }
        override suspend fun second(input: Sequence<String>): Any {
            Thread.sleep(SECOND_SLEEP_TIME)
            return input.drop(1).first()
        }
    }
}
