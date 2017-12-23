package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.scan

/**
 *
 * @author Jelle Stege
 */
class Day01 : Day() {
    override val title: String = "Not Quite Lisp"
    
    override fun first(input: Sequence<String>): Any = input.head
            .asSequence()
            .map { if (it == '(') 1 else -1 }
            .sum()

    override fun second(input: Sequence<String>): Any = input.head
            .asSequence()
            .map { if (it == '(') 1 else -1 }
            .scan(0, Int::plus)
            .takeWhile { it >= 0 }
            .count()
}
