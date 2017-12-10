package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day10 : Day() {
    private companion object Configuration {
        private const val FIRST_ELEMENTS_TO_MULTIPLY = 2
        private val SECOND_APPENDIX = listOf(17, 31, 73, 47, 23)
        private const val SECOND_STRETCH_ITERATIONS = 64
        private const val SECOND_BLOCK_SIZE = 16
    }

    override fun first(input: Sequence<String>): Any {
        return input.head
                .split(",")
                .map { it.toInt() }
                .asSequence()
                .toSparseHash()
                .take(FIRST_ELEMENTS_TO_MULTIPLY)
                .reduce(Int::times)
    }

    override fun second(input: Sequence<String>): Any {
        return input.head
                .map { it.toInt() and 0xFF }
                .plus(SECOND_APPENDIX)
                .asSequence()
                .times(SECOND_STRETCH_ITERATIONS)
                .toSparseHash()
                .chunked(SECOND_BLOCK_SIZE)
                .map { it.reduce(Int::xor).toByte() }
                .toHexString()
    }

    private fun Sequence<Int>.toSparseHash() = this
            .foldIndexed((0..255).toList() to 0) { skipSize, (list, currentPos), length ->
                list.reverse(currentPos, length) to (currentPos + length + skipSize) % list.size
            }.first

    private fun <E> List<E>.reverse(start: Int, length: Int): List<E> =
            (start until start + length)
                    .map { it % this.size }
                    .zipWithReverse()
                    .asSequence()
                    .take(length / 2)
                    .fold(this.toMutableList()) { list, (f, s) ->
                        list.swap(f, s)
                        list
                    }
}
