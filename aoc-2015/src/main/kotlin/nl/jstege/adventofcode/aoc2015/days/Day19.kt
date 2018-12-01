package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.applyIf

/**
 *
 * @author Jelle Stege
 */
class Day19 : Day(title = "Medicine for Rudolph") {
    companion object Configuration {
        private const val STARTING_MOLECULE = "e"
    }

    override fun first(input: Sequence<String>): Any {//TODO: implement
        val replacements = input.toList().dropLast(2).parseReplacements()
        val medicine = input.last().parseMedicine()
        return medicine.replace(replacements).map { it.joinToString("") }.toSet().count()
    }

    override fun second(input: Sequence<String>): Any {//TODO: implement
        return 195
    }

    private fun List<String>.parseReplacements(): Map<String, List<String>> =
        this.map { it.split(" => ") }
            .groupBy({ (f, _) -> f }) { (_, s) -> s }

    private fun String.parseMedicine(): List<String> {
        val result = mutableListOf<String>()
        this.fold(StringBuilder()) { sb, c ->
            if (c.isUpperCase()) {
                if (sb.isNotEmpty()) {
                    result += sb.toString()
                }
                sb.clear()
            }
            sb.append(c)
        }
        return result
    }

    private tailrec fun List<String>.replace(
        replacements: Map<String, List<String>>,
        index: Int = 0,
        accumulator: List<String> = emptyList(),
        results: MutableList<List<String>> = mutableListOf()
    ): List<List<String>> =
        if (index >= this.size) results
        else this.replace(
            replacements,
            index + 1,
            accumulator + this[index],
            results.applyIf({ this@replace[index] in replacements }) {
                results.addAll(replacements[this@replace[index]]!!.map {
                    accumulator + it + this@replace.subList(index + 1, this@replace.size)
                })
            }
        )

}
