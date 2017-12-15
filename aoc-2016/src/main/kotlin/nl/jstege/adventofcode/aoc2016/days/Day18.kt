package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.replace
import nl.jstege.adventofcode.aoccommon.utils.extensions.scan
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

        private val TRAP = '^' to '1'
        private val SAFE = '.' to '0'
    }

    override fun first(input: Sequence<String>): Any = input.walk(ITERATIONS_FIRST)

    override fun second(input: Sequence<String>): Any = input.walk(ITERATIONS_SECOND)

    private fun Sequence<String>.walk(iterations: Int) = this.first()
            .replace(TRAP, SAFE)
            .toBigInteger(radix = 2)
            .computeRule90(iterations)


    private fun BigInteger.computeRule90(iterations: Int): Int = (0 until iterations)
            .asSequence()
            .scan(this) { row, _ -> (row shr 1) xor ((row shl 1) and ROW_MASK) }
            .sumBy { ROW_LENGTH - it.bitCount() }

}
