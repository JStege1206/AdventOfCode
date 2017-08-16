package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.floorMod

/**
 *
 * @author Jelle Stege
 */
class Day15 : Day() {
    private val INPUT_REGEX =
            "Disc #(\\d+) has (\\d+) positions; at time=0, it is at position (\\d+)\\."
                    .toRegex()

    private val SECOND_EXTRA_DISC = "Disc #7 has 11 positions; at time=0, it is at position 0."

    override fun first(input: Sequence<String>): Any = input.parse().calculateCrt()

    override fun second(input: Sequence<String>): Any = (input + SECOND_EXTRA_DISC).parse().calculateCrt()


    private fun Sequence<String>.parse(): List<Disc> = this
            .map { INPUT_REGEX.matchEntire(it)?.groupValues!! }
            .map { it.map { it.toLongOrNull() ?: 0 } }
            .map { (_, a, n, aMod) -> Disc(n, (-a - aMod) floorMod n) }
            .toList()

    private fun List<Disc>.calculateCrt(): Long {
        val N = this.map { it.n }.fold(1L, Long::times)

        var i = 0
        var x: Long = 0
        var inc: Long = 1
        while (i < this.size && x < N) {
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