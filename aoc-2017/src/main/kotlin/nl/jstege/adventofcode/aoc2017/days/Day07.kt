package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues

/**
 *
 * @author Jelle Stege
 */
class Day07 : Day(title = "Recursive Circus") {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING = """(\w+) \((\d+)\)( -> ([\w, ]+))?"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private const val NAME_INDEX = 1
        private const val WEIGHT_INDEX = 2
        private const val NODES_INDEX = 4

        private val PARAM_INDICES = intArrayOf(NAME_INDEX, WEIGHT_INDEX, NODES_INDEX)
    }

    override fun first(input: Sequence<String>): Any = input.findRoot().name

    override fun second(input: Sequence<String>): Any = input.findRoot().findWrongWeight()

    private fun Sequence<String>.findRoot(): Program {
        val cache = mutableMapOf<String, Program>()
        return this
            .map { it.extractValues(INPUT_REGEX, *PARAM_INDICES) }
            .map { (name, weight, nodes) ->
                cache.getOrPut(name) { Program(name) }.apply {
                    this.weight = weight.toInt()
                    if (nodes.isNotBlank()) {
                        this.children += nodes
                            .split(", ").asSequence()
                            .map { cache.getOrPut(it) { Program(it) } }
                            .onEach { it.parent = this }
                            .toList()
                    }
                }
            }
            .toList()
            .first { it.parent == null }
    }

    private tailrec fun Program.findWrongWeight(): Int {
        val groups = children.groupBy { it.totalWeight }
        val wrongNode = groups.values.minBy { it.size }!!.first()

        if (wrongNode.children.map { it.totalWeight }.toSet().size < 2) {
            return groups.values.maxBy { it.size }!!.first()
                .totalWeight - (wrongNode.totalWeight - wrongNode.weight)
        }
        return wrongNode.findWrongWeight()
    }

    private data class Program(
        val name: String,
        var weight: Int = 0,
        var parent: Program? = null,
        val children: MutableSet<Program> = mutableSetOf()
    ) {
        val totalWeight: Int by lazy { weight + children.map { it.totalWeight }.sum() }

        override fun equals(other: Any?): Boolean =
            other != null && other is Program && other.name == name

        override fun hashCode(): Int = name.hashCode()
    }
}

