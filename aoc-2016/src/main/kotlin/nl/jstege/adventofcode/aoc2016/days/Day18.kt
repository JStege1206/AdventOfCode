package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.replace
import nl.jstege.adventofcode.aoccommon.utils.extensions.shl
import nl.jstege.adventofcode.aoccommon.utils.extensions.shr
import nl.jstege.adventofcode.aoccommon.utils.extensions.xor
import nl.jstege.adventofcode.aoccommon.utils.extensions.and
import nl.jstege.adventofcode.aoccommon.utils.extensions.toBigInteger
import java.math.BigInteger

/**
 *
 * @author Jelle Stege
 */
class Day18 : Day() {
    private companion object Configuration {
        private const val ROW_LENGTH = 100
        private val ROW_MASK = (BigInteger.ONE shl ROW_LENGTH) - BigInteger.ONE

        private const val ITERATIONS_FIRST = 40
        private const val ITERATIONS_SECOND = 400000
    }

    override fun first(input: Sequence<String>): Any = input.first()
            .replace(listOf('^', '.'), listOf('1', '0'))
            .toBigInteger(radix = 2)
            .computeRule90(ITERATIONS_FIRST)


    override fun second(input: Sequence<String>): Any = input.first()
            .replace(listOf('^', '.'), listOf('1', '0'))
            .toBigInteger(radix = 2)
            .computeRule90(ITERATIONS_SECOND)

    private fun BigInteger.computeRule90(iterations: Int): Int = (0 until iterations)
            .fold(Pair(this, 0), { (row, sum), _ ->
                Pair((row shr 1) xor ((row shl 1) and ROW_MASK),
                        sum + (ROW_LENGTH - row.bitCount())
                )
            }).second
}
