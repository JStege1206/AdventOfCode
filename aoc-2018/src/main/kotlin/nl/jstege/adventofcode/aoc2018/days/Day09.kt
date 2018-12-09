package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import java.util.*

class Day09 : Day(title = "Marble Mania") {
    private companion object Configuration {
        private val INPUT_REGEX = """^(\d+) players; last marble is worth (\d+) points""".toRegex()
        
        private const val MULTIPLE_OF = 23
        private const val MOVE_STEPS_LEFT = 7
        private const val LAST_MARBLE_FACTOR = 100
    }

    override fun first(input: Sequence<String>): Any =
        input.head
            .extractValues(INPUT_REGEX, 1, 2)
            .map(String::toInt)
            .let { (players, maxValue) -> run(players, maxValue) }

    override fun second(input: Sequence<String>): Any =
        input.head
            .extractValues(INPUT_REGEX, 1, 2)
            .map(String::toInt)
            .let { (players, maxValue) -> run(players, maxValue * LAST_MARBLE_FACTOR) }

    private fun run(players: Int, maxValue: Int): Long {
        val marbles = MarbleCircle(0)
        val scores = LongArray(players) { 0 }

        for (newMarble in 1..maxValue) {
            if (newMarble % MULTIPLE_OF == 0) {
                marbles.left(MOVE_STEPS_LEFT - 1)
                scores[newMarble % players] += marbles.remove().toLong() + newMarble
            } else {
                marbles.right(2)
                marbles.addLast(newMarble)
            }
        }
        return scores.max()!!
    }

    private class MarbleCircle(
        vararg init: Int,
        private val deque: ArrayDeque<Int> = ArrayDeque<Int>().apply { addAll(init.toList()) }
    ) : Deque<Int> by deque {
        fun right(n: Int = 1) = repeat(n) { addFirst(removeLast()) }
        fun left(n: Int = 1) = repeat(n) { addLast(removeFirst()) }
    }
}
