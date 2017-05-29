package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day02 : Day() {
    override fun first(input: Sequence<String>) = input
            .map { it.split('x').map { it.toInt() } }
            .map { (l, w, h) ->
                2 * l * w + 2 * w * h + 2 * h * l + Math.min(Math.min(w * l, w * h), l * h)
            }
            .sum()

    override fun second(input: Sequence<String>) = input
            .map { it.split('x').map { it.toInt() } }
            .map { (l, w, h) ->
                2 * Math.min(Math.min(l, w), h) +
                        2 * Math.max(Math.min(w, l), Math.min(Math.max(w, l), h)) +
                        l * w * h
            }
            .sum()
}