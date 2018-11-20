package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.transpose

/**
 *
 * @author Jelle Stege
 */
class Day06 : Day(title = "Signals and Noise") {
    override fun first(input: Sequence<String>): Any = input.toList()
        .errorCorrect(Map<Char, List<Char>>::minBy)

    override fun second(input: Sequence<String>): Any = input.toList()
        .errorCorrect(Map<Char, List<Char>>::maxBy)

    private fun List<String>.errorCorrect(
        selector: Map<Char, List<Char>>.((Map.Entry<Char, List<Char>>) -> Int) ->
        Map.Entry<Char, List<Char>>?
    ) =
        this
            .map(String::toList)
            .transpose()
            .map { column -> column.groupBy { it }.selector { it.value.size }?.key }
}

