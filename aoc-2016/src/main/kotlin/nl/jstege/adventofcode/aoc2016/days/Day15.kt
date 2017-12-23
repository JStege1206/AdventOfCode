package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.floorMod

/**
 *
 * @author Jelle Stege
 */
class Day15 : Day() {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING =
                """Disc #(\d+) has (\d+) positions; at time=0, it is at position (\d+)\."""
        private const val SECOND_EXTRA_DISC =
                "Disc #7 has 11 positions; at time=0, it is at position 0."
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
    }

    override val title: String = "Timing is Everything"

    override fun first(input: Sequence<String>): Any = input
            .parse().calculateCrt()

    override fun second(input: Sequence<String>): Any = (input + SECOND_EXTRA_DISC)
            .parse().calculateCrt()


    private fun Sequence<String>.parse(): List<Disc> = this
            .map { INPUT_REGEX.matchEntire(it)?.groupValues!! }
            .map { it.map { it.toLongOrNull() ?: 0 } }
            .map { (_, a, n, aMod) -> Disc(n, (-a - aMod) floorMod n) }
            .toList()

    private fun List<Disc>.calculateCrt(): Long {
        val maxN = this.map { it.n }.reduce(Long::times)

        var i = 0
        var x = 0L
        var inc = 1L
        while (i < this.size && x < maxN) {
            if (x % this[i].n == this[i].a) {
                inc *= this[i].n
                i++
            } else {
                x += inc
            }
        }
        return x
    }

    private data class Disc(val n: Long, val a: Long)
}
