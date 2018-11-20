package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues
import nl.jstege.adventofcode.aoccommon.utils.extensions.transformTo

/**
 *
 * @author Jelle Stege
 */
class Day14 : Day(title = "Reindeer Olympics") {
    private companion object Configuration {
        private const val TRAVEL_TIME = 2503
        private const val INPUT_PATTERN_STRING =
            """\w+ can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\."""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private const val SPEED_INDEX = 1
        private const val TRAVEL_TIME_INDEX = 2
        private const val REST_TIME_INDEX = 3

        private val PARAM_INDICES = intArrayOf(SPEED_INDEX, TRAVEL_TIME_INDEX, REST_TIME_INDEX)
    }

    override fun first(input: Sequence<String>) = input
        .parse()
        .asSequence()
        .map { it.travel(TRAVEL_TIME).distanceTravelled }
        .max()!!

    override fun second(input: Sequence<String>) =
        (1..TRAVEL_TIME)
            .transformTo(input.parse().toList()) { rs, i ->
                rs.onEach { it.travel(i) }
                    .filter { r -> r.distanceTravelled == rs.map { it.distanceTravelled }.max() }
                    .forEach { it.score++ }
            }
            .asSequence()
            .map { it.score }
            .max()!!


    private fun Sequence<String>.parse() = this
        .map { it.extractValues(INPUT_REGEX, *PARAM_INDICES) }
        .map { (speed, travelTime, restTime) ->
            Reindeer(speed.toInt(), travelTime.toInt(), restTime.toInt())
        }

    private data class Reindeer(val speed: Int, val travelTime: Int, val restTime: Int) {
        var distanceTravelled = 0
        var score = 0

        fun travel(currentTime: Int): Reindeer {
            distanceTravelled = speed * (currentTime / (travelTime + restTime) * travelTime +
                    (Math.min(currentTime % (travelTime + restTime), travelTime)))
            return this
        }
    }
}
