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
        ?.let { (guardId, sleepSessions) ->
            sleepSessions.findMinuteMostAsleep()?.key?.let { guardId * it }
        } ?: throw IllegalStateException()

    override fun second(input: Sequence<String>): Any = input
        .parse()
        .mapNotNull { (guardId, sleepSessions) ->
            sleepSessions
                .findMinuteMostAsleep()
                ?.let { (minute, amount) -> Triple(guardId, minute, amount) }
        }
        .maxBy { (_, _, minuteAmount) -> minuteAmount }
        ?.let { (guardId, minute) ->
            guardId * minute
        } ?: throw IllegalStateException()

    private fun Sequence<String>.parse(): Map<Int, List<List<Int>>> =
        mutableMapOf<Int, MutableList<List<Int>>>().apply {
            this@parse
                .sorted()
                .map { it.extractValues(INPUT_REGEX, 2, 3, 4) }
                .fold(-1 to mutableListOf<List<Int>>()) { (start, sessions), (minute, op, id) ->
                    when (op) {
                        "falls asleep" -> minute.toInt() to sessions
                        "wakes up" -> -1 to sessions.apply { add(start.minutesTo(minute.toInt())) }
                        else -> -1 to computeIfAbsent(id.toInt()) { mutableListOf() }
                    }
                }
        }

    private fun Int.minutesTo(other: Int): List<Int> =
        if (this >= other) (this until (other + 60)).map { it % 60 }
        else (this until other).toList()

    private fun List<List<Int>>.findMinuteMostAsleep(): Map.Entry<Int, Int>? = this
        .flatten()
        .groupBy { it }
        .mapValues { (_, value) -> value.size }
        .maxBy { it.value }
}
