package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.foldWhile
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.takeWhileSumGreaterThan

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
            .takeWhileSumGreaterThan(-2) // -2 to also count the last element.
            .size

}
