package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

/**
 *
 * @author Jelle Stege
 */
class Day08 : Day() {
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
    }


    override suspend fun first(input: Sequence<String>): Any {
        return input
                .parse()
                .fold(emptyMap<String, Int>()) { rs, instr -> rs + (instr.r1 to instr(rs)) }
                .values
                .max() ?: 0
    }


    override suspend fun second(input: Sequence<String>): Any {

        return input
                .parse()
                .fold(emptyMap<String, Int>() to Int.MIN_VALUE) { (rs, max), instr ->
                    val result = instr(rs)
                    (rs + (instr.r1 to result)) to max(result, max)
                }
                .second
    }


    private fun Sequence<String>.parse() = this
            .map { INPUT_REGEX.matchEntire(it)?.groupValues!! }
            .map { (_, r1, op, l1, r2, test, l2) ->
                Instruction(r1, OPERATIONS[op]!!, l1.toInt(), r2, TESTS[test]!!, l2.toInt())
            }

    private data class Instruction(
            val r1: String,
            val op: (Int, Int) -> Int,
            val l1: Int,
            val r2: String,
            val test: (Int, Int) -> Boolean,
            val l2: Int) {
        operator fun invoke(registers: Map<String, Int>): Int =
                if (test(registers[r2] ?: 0, l2)) {
                    op(registers[r1] ?: 0, l1)
                } else {
                    registers[r1] ?: 0
                }
    }
}
