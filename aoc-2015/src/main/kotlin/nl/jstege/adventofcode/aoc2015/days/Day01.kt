package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.scan

/**
 *
 * @author Jelle Stege
 */
class Day01 : Day() {
    override fun first(input: Sequence<String>): Any = input.head
            .map { if (it == '(') 1 else -1 }
            .sum()

    override fun second(input: Sequence<String>): Any = input.head
            .map { if (it == '(') 1 else -1 }.asSequence()
            .scan(0 to 0) { (floor, els), i -> (floor + i) to (els + 1) }
            .takeWhile { (floor, _) -> floor >= -1 }
            .last().second
}
