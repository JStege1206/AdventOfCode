package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day14 : Day() {
    val INPUT_REGEX = ("\\w+ can fly (\\d+) km/s for (\\d+) seconds, " +
            "but then must rest for (\\d+) seconds\\.").toRegex()

    private val TRAVEL_TIME = 2503

    override fun first(input: Sequence<String>) = input
            .map {
                val i = INPUT_REGEX.matchEntire(it)?.groupValues
                        ?: throw kotlin.IllegalArgumentException("Invalid input")
                Triple(i[1].toInt(), i[2].toInt(), i[3].toInt())
            }
            .map { (speed, travelTime, restTime) ->
                speed * (TRAVEL_TIME / (travelTime + restTime) * travelTime +
                        (Math.min(TRAVEL_TIME % (travelTime + restTime), travelTime)))
            }
            .max()!!

    override fun second(input: Sequence<String>) =
            (1..TRAVEL_TIME)
            .fold(input.parse().toList(), { rs, i ->
                rs.onEach { it.travel(i) }.filter {
                    it.distanceTravelled == rs.maxBy { it.distanceTravelled }!!.distanceTravelled
                }.forEach { it.score++ }
                rs
            })
            .maxBy { it.score }!!.score


    private fun Sequence<String>.parse() = this
            .map {
                INPUT_REGEX.matchEntire(it)?.groupValues
                        ?: throw IllegalArgumentException("Invalid input")
            }
            .map { (_, speed, tt, rt) ->
                Reindeer(speed.toInt(), tt.toInt(), rt.toInt())
            }

    data class Reindeer(val speed: Int, val travelTime: Int, val restTime: Int) {
        var distanceTravelled = 0
        var score = 0

        fun travel(current: Int) {
            distanceTravelled = speed * (current / (travelTime + restTime) * travelTime +
                    (Math.min(current % (travelTime + restTime), travelTime)))
        }
    }
}