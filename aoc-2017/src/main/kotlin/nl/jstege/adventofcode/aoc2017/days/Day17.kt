package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.scan

/**
 *
 * @author Jelle Stege
 */
class Day17 : Day() {
    private companion object Configuration {
        private const val FIRST_ITERATIONS = 2017
        private const val SECOND_ITERATIONS = 50_000_000
        private const val SECOND_POSITION_WANTED = 1
    }

    override fun first(input: Sequence<String>): Any {
        return input.head.toInt().let { steps ->
            mutableListOf(0).let { list ->
                (1..FIRST_ITERATIONS)
                        .scan(0) { position, it -> ((position + steps) % it) + 1 }
                        .withIndex()
                        .onEach { list.add(it.value, it.index) }
                        .last()
                        .let { list[it.value + 1] }
            }
        }
    }

    override fun second(input: Sequence<String>): Any {
        return input.head.toInt().let { steps ->
            (1..SECOND_ITERATIONS)
                    .asSequence()
                    .scan(0) { current, it -> ((steps + current) % it) + 1 }
                    .withIndex()
                    .last { it.value == SECOND_POSITION_WANTED }
                    .index
        }
    }

}
