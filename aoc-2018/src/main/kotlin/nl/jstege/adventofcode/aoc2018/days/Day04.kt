package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues

class Day04 : Day(title = "Repose Record") {
    companion object Configuration {
        private const val INPUT_PATTERN_STRING =
            """\[(\d{4}-\d{2}-\d{2} \d{2}:(\d{2}))] """ +
                    """(Guard #(\d+) begins shift|falls asleep|wakes up)"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
    }

    override fun first(input: Sequence<String>): Any = input
        .parse()
        .maxBy { (_, sleepSessions) -> sleepSessions.sumBy { it.size } }
        ?.let { (guard, sleepSessions) ->
            sleepSessions.findMinuteMostAsleep()?.key?.let { guard * it }
        } ?: throw IllegalStateException()

    override fun second(input: Sequence<String>): Any = input
        .parse()
        .mapValues { (_, sleepSessions) ->
            sleepSessions
                .findMinuteMostAsleep()
                ?.let { (minute, amount) -> minute to amount } ?: (0 to 0)
        }
        .maxBy { (_, minuteAmount) -> minuteAmount.second }
        ?.let { (g, p) -> g * p.first } ?: throw IllegalStateException()

    private fun Sequence<String>.parse() = mutableMapOf<Int, MutableList<List<Int>>>().apply {
        this@parse
            .sorted()
            .map { it.extractValues(INPUT_REGEX, 2, 3, 4) }
            .fold(-1 to mutableListOf<List<Int>>()) { (startTime, sessions), (minute, op, id) ->
                when (op) {
                    "falls asleep" -> minute.toInt() to sessions
                    "wakes up" -> -1 to sessions.apply { add(startTime.minutesTo(minute.toInt())) }
                    else -> -1 to computeIfAbsent(id.toInt()) { mutableListOf() }
                }
            }
    }

    private fun Int.minutesTo(other: Int) =
        if (this >= other) (this until (other + 60)).map { it % 60 }
        else (this until other).toList()

    private fun List<List<Int>>.findMinuteMostAsleep() = this
        .flatten()
        .groupingBy { it }
        .eachCount()
        .maxBy { it.value }
}
