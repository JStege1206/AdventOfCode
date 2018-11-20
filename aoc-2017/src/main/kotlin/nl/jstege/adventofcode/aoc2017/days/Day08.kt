package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day08 : Day(title = "I Heard You Like Registers") {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING =
            """(\w+) (inc|dec) ([-\d]+) if (\w+) ([=!><]+) ([-\d]+)"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
        private val OPERATIONS = mapOf<String, (Int, Int) -> Int>(
            "inc" to Int::plus,
            "dec" to Int::minus
        )

        private val TESTS = mapOf(
            "==" to Comparable<Int>::equals,
            "!=" to Comparable<Int>::notEquals,
            ">" to Comparable<Int>::greaterThan,
            "<" to Comparable<Int>::lessThan,
            ">=" to Comparable<Int>::greaterThanEquals,
            "<=" to Comparable<Int>::lessThanEquals
        )

        private const val R1_INDEX = 1
        private const val OP_INDEX = 2
        private const val L1_INDEX = 3
        private const val R2_INDEX = 4
        private const val TEST_INDEX = 5
        private const val L2_INDEX = 6

        private val PARAM_INDICES = intArrayOf(
            R1_INDEX,
            OP_INDEX,
            L1_INDEX,
            R2_INDEX,
            TEST_INDEX,
            L2_INDEX
        )
    }

    override fun first(input: Sequence<String>): Any {
        return input
            .parse()
            .transformTo(mutableMapOf<String, Int>()) { rs, instr ->
                rs.apply { this[instr.r1] = instr(rs) }
            }
            .values
            .max() ?: 0
    }


    override fun second(input: Sequence<String>): Any {
        return input
            .parse()
            .fold(mutableMapOf<String, Int>() to Int.MIN_VALUE) { (rs, max), instr ->
                instr(rs).let { result ->
                    rs.apply { this[instr.r1] = result } to max(result, max)
                }
            }
            .second
    }


    private fun Sequence<String>.parse() = this
        .map { it.extractValues(INPUT_REGEX, *PARAM_INDICES) }
        .map { (r1, op, l1, r2, test, l2) ->
            Instruction(r1, OPERATIONS[op]!!, l1.toInt(), r2, TESTS[test]!!, l2.toInt())
        }

    private data class Instruction(
        val r1: String,
        val op: (Int, Int) -> Int,
        val l1: Int,
        val r2: String,
        val test: (Int, Int) -> Boolean,
        val l2: Int
    ) {
        operator fun invoke(registers: Map<String, Int>): Int =
            if (test(registers[r2] ?: 0, l2)) op(registers[r1] ?: 0, l1)
            else registers[r1] ?: 0
    }
}
