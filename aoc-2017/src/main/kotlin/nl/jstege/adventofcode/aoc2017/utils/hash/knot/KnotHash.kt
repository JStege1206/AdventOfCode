package nl.jstege.adventofcode.aoc2017.utils.hash.knot

import nl.jstege.adventofcode.aoccommon.utils.extensions.reverse
import nl.jstege.adventofcode.aoccommon.utils.extensions.times

object KnotHash {
    fun String.knotHash(): ByteArray = this
            .map { it.toInt() and 0xFF }
            .plus(listOf(17, 31, 73, 47, 23))
            .times(STRETCH_ITERATIONS)
            .scramble()
            .chunked(BLOCK_SIZE)
            .map { it.reduce(Int::xor).toByte() }
            .toByteArray()

    fun List<Int>.scramble(): List<Int> = this
            .foldIndexed((0..255).toList() to 0) { skipSize, (list, currentPos), length ->
                list.reverse(currentPos, length) to (currentPos + length + skipSize) % list.size
            }.first
}

private const val BLOCK_SIZE = 16
private const val STRETCH_ITERATIONS = 64
