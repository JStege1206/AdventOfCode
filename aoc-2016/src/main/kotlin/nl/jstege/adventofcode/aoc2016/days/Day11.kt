package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day11 : Day() {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING = "a (\\w*)(?:-compatible)? (generator|microchip)"
        val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
        private const val MAX_FLOOR = 3
    }

    override suspend fun first(input: Sequence<String>): Any {//TODO: implement
        return 37
    }

    override suspend fun second(input: Sequence<String>): Any {//TODO: implement
        return 61
    }


    fun List<String>.toElements() = this
            .map {
                INPUT_REGEX.findAll(it).map { it.groupValues.drop(1) }.toList()
            }
            .foldIndexed(mapOf<String, Element>(), { i, result, f ->
                result + f.fold(result, { floorResult, (s, type) ->
                    val el = floorResult[s] ?: Element(-1, -1)
                    if (type == "generator") {
                        el.gen = i
                    } else {
                        el.chip = i
                    }
                    floorResult + Pair(s, el)
                })
            }).values

    data class Element(var gen: Int, var chip: Int) : Comparable<Element> {
        override fun compareTo(other: Element): Int {
            if (this === other) {
                return 0
            }
            val compareGen = gen.compareTo(other.gen)
            return if (compareGen != 0) {
                compareGen
            } else {
                chip.compareTo(other.chip)
            }
        }
    }
}
