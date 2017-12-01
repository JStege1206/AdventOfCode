package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day01 : Day() {
    override fun first(input: Sequence<String>): Any {
        return (input.head + input.head.first())
                .map { it - '0' }
                .zipWithNext()
                .filter { (x, y) -> x == y }
                .sumBy { (x, _) -> x }
    }

    override fun second(input: Sequence<String>): Any {
        val digits = input.head.map { it - '0' }
        return digits
                .take(digits.size / 2) //Second half of the list is equal to the first half
                .zip(digits.drop(digits.size / 2))
                .filter { (x, y) -> x == y }
                .sumBy { (x, _) -> x } * 2 //Times two since we only checked the first half.
    }
}
