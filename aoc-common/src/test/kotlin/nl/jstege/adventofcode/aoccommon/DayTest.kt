package nl.jstege.adventofcode.aoccommon

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.DayTester

/**
 *
 * @author Jelle Stege
 */
class DayTest : DayTester(DayTestImpl()) {
    class DayTestImpl : Day() {
        companion object Configuration {
            val FIRST_SLEEP_TIME = 1000L
            val SECOND_SLEEP_TIME = 1500L
        }

        override val title: String = "Test Implementation"

        override fun first(input: Sequence<String>): Any {
            Thread.sleep(FIRST_SLEEP_TIME)
            return input.first()
        }

        override fun second(input: Sequence<String>): Any {
            Thread.sleep(SECOND_SLEEP_TIME)
            return input.drop(1).first()
        }
    }
}
