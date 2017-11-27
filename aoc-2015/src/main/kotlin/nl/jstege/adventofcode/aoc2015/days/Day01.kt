package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.foldWhile
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

/**
 *
 * @author Jelle Stege
 */
class Day01 : Day() {
    override fun first(input: Sequence<String>): Any = input.head
            .map { if (it == '(') 1 else -1 }
            .sum()

    override fun second(input: Sequence<String>): Any = input.head
            .map { if (it == '(') 1 else -1 }
            .foldWhile(
                    0 to 0,
                    { (floor, _), i -> floor + i >= -1 }, // predicate
                    { (floor, els), i -> (floor + i) to (els + 1) } // operation
            ).second

}
