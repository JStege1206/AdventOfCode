package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.cycle
import nl.jstege.adventofcode.aoccommon.utils.extensions.scan

class Day01 : Day(title = "Chronal Calibration") {
    private companion object Configuration {
        private const val STARTING_FREQUENCY = 0
    }

    override fun first(input: Sequence<String>): Any =
        input.map(String::toInt).sum() + STARTING_FREQUENCY

    override fun second(input: Sequence<String>): Any =
        mutableSetOf<Int>().let { frequencies ->
            input
                .map(String::toInt)
                .cycle()
                .scan(STARTING_FREQUENCY, Int::plus)
                .first { !frequencies.add(it) }
        }
}
