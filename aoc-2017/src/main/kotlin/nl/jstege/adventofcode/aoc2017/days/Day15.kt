package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day15 : Day() {
    private companion object Configuration {
        private const val MASK = 0xFFFFL
        private const val DIVISOR = 2147483647

        private const val FACTOR_A = 16807L
        private const val FACTOR_B = 48271L

        private const val FIRST_ITERATIONS = 40_000_000
        private const val SECOND_ITERATIONS = 5_000_000

        private const val SECOND_MULTIPLES_A = 4L
        private const val SECOND_MULTIPLES_B = 8L
    }

    override fun first(input: Sequence<String>): Any {
        return input
                .map { it.split(" ") }
                .map { (_, _, _, _, init) -> init.toLong() }
                .toList()
                .let { (a, b) -> Generator(a, FACTOR_A) to Generator(b, FACTOR_B) }
                .let { (a, b) ->
                    (0 until FIRST_ITERATIONS).count { a.next() and MASK == b.next() and MASK }
                }
    }

    override fun second(input: Sequence<String>): Any {
        return input
                .map { it.split(" ") }
                .map { (_, _, _, _, init) -> init.toLong() }
                .toList()
                .let { (a, b) -> Generator(a, FACTOR_A) to Generator(b, FACTOR_B) }
                .let { (a, b) ->
                    (0 until SECOND_ITERATIONS).count {
                        a.next(SECOND_MULTIPLES_A) and MASK == b.next(SECOND_MULTIPLES_B) and MASK
                    }
                }
    }

    private class Generator(init: Long, val factor: Long) {
        var current = init

        fun next(multiples: Long = 1): Long {
            do {
                current = (current * factor % DIVISOR)
            } while (current % multiples != 0L)
            return current
        }
    }
}
