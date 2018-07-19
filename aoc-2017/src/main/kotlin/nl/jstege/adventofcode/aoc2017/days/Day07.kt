package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day07 : Day(title = "Recursive Circus") {
    private companion object Configuration {
        private const val INPUT_PATTERN_STRING = """(\w+) \((\d+)\)( -> ([\w, ]+))?"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()
    }

    override fun first(input: Sequence<String>): Any = input.findRoot().name

    override fun second(input: Sequence<String>): Any = input.findRoot().findWrongWeight()

    private fun Sequence<String>.findRoot(): Program {
        val cache = mutableMapOf<String, Program>()
        return this
            .map { INPUT_REGEX.matchEntire(it)?.groupValues!! }
            .map { (_, name, weight, _, nodes) ->
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

