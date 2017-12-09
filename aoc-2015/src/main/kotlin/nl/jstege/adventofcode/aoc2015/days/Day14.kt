package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day14 : Day() {
    private companion object Configuration {
        private const val TRAVEL_TIME = 2503
        private const val INPUT_PATTERN_STRING = "\\w+ can fly (\\d+) km/s for (\\d+) seconds, " +
                "but then must rest for (\\d+) seconds\\."
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
    }

    override fun first(input: Sequence<String>) = input.parse()
            .map { it.travel(TRAVEL_TIME).distanceTravelled }
            .max()!!

    override fun second(input: Sequence<String>) =
            (1..TRAVEL_TIME)
                    .fold(input.parse().toList()) { rs, i ->
                        rs.onEach { it.travel(i) }
                                .filter {
                                    it.distanceTravelled == rs.map { it.distanceTravelled }.max()
                                }
                                .forEach { it.score++ }
                        rs
                    }
                    .map { it.score }
                    .max()!!


    private fun Sequence<String>.parse() = this
            .map { INPUT_REGEX.matchEntire(it)?.groupValues!! }
            .map { (_, speed, travelTime, restTime) ->
                Reindeer(speed.toInt(), travelTime.toInt(), restTime.toInt())
            }

    private data class Reindeer(val speed: Int, val travelTime: Int, val restTime: Int) {
        var distanceTravelled = 0
        var score = 0

        fun travel(current: Int): Reindeer {
            distanceTravelled = speed * (current / (travelTime + restTime) * travelTime +
                    (Math.min(current % (travelTime + restTime), travelTime)))
            return this
        }
    }
}
