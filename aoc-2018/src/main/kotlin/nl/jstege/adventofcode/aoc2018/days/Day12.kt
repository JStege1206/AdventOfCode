package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.sumBy

class Day12 : Day(title = "Subterranean Sustainability") {
    private companion object Configuration {
        private const val FIRST_GENERATIONS = 20L
        private const val SECOND_GENERATIONS = 50_000_000_000L
    }

    override fun first(input: Sequence<String>): Any =
        input.parse().let { (initial, reps) ->
            initial.solve(FIRST_GENERATIONS, reps)
        }

    override fun second(input: Sequence<String>): Any =
        input.parse().let { (initial, reps) ->
            initial.solve(SECOND_GENERATIONS, reps)
        }

    private fun String.solve(generations: Long, replacements: Set<String>): Long {
        tailrec fun String.solveRecursive(currentGen: Long, shift: Long): Long {
            val (nextState, nextShift) = this.next(shift, replacements)
            if (nextState == this || currentGen == generations) {
                val absoluteShift = shift + (nextShift - shift) * (generations - currentGen)
                return this
                    .withIndex()
                    .filter { it.value == '#' }
                    .sumBy { it.index + absoluteShift }
            }
            return nextState.solveRecursive(currentGen + 1, nextShift)
        }
        return this.solveRecursive(0, 0)
    }

    private fun String.next(shift: Long, replacements: Set<String>): Pair<String, Long> =
        "....$this...."
            .windowed(5, 1)
            .joinToString("") { if (it in replacements) "#" else "." }
            .let { state ->
                Pair(state.trim('.'), shift - 2 + state.indexOf('#'))
            }

    private fun Sequence<String>.parse(): Pair<String, Set<String>> =
        Pair(
            head.removePrefix("initial state: "),
            drop(2)
                .map { it.split(" => ") }
                .filter { (_, s) -> s != "." }
                .map { it.first() }
                .toSet()
        )
}
