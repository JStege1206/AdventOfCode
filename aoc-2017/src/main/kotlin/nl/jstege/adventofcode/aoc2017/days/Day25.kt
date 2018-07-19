package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValue

/**
 *
 * @author Jelle Stege
 */
class Day25 : Day(title = "The Halting Problem") {
    companion object Configuration {
        private val BEGIN_REGEX =
            """Begin in state ([A-Z]).""".toRegex()
        private val ITERATIONS_REGEX =
            """Perform a diagnostic checksum after ([0-9]+) steps.""".toRegex()
        private val STATE_INIT_REGEX =
            """In state ([A-Z]):""".toRegex()
        private val STATE_WRITE_REGEX =
            """- Write the value ([0-1]).""".toRegex()
        private val STATE_MOVE_REGEX =
            """- Move one slot to the (right|left).""".toRegex()
        private val STATE_NEXT_REGEX =
            """- Continue with state ([A-Z]).""".toRegex()

    }

    override fun first(input: Sequence<String>): Any {
        return input
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toList()
            .parseAndCalculateChecksum()

    }

    override fun second(input: Sequence<String>): Any {
        return "-"
    }

    private fun List<String>.parseAndCalculateChecksum(): Int {
        val beginState = this[0].extractValue(BEGIN_REGEX, 1)
        val iterations = this[1].extractValue(ITERATIONS_REGEX, 1).toInt()

        val states = this
            .drop(2) //Drop the 2 starting lines: "Begin in state.." and "Perform a diagnostic..."
            .chunked(9) //Split input into groups of 9 lines, which indicate a state.
            .associateBy(
                { it[0].extractValue(STATE_INIT_REGEX, 1) },
                {
                    State(
                        Instruction.parse(it.subList(2, 5)),
                        Instruction.parse(it.subList(6, 9))
                    )
                }
            )

        return TuringMachine(states[beginState]!!, states)
            .apply { this.run(iterations) }
            .checksum
    }


    private data class Instruction(
        val value: Int,
        val direction: RelativeDirection,
        val next: String
    ) {
        companion object {
            fun parse(raw: List<String>) = Instruction(
                raw[0].extractValue(STATE_WRITE_REGEX, 1).toInt(),
                raw[1].extractValue(STATE_MOVE_REGEX, 1).toRelativeDirection(),
                raw[2].extractValue(STATE_NEXT_REGEX, 1)
            )

            private fun String.toRelativeDirection() = RelativeDirection.valueOf(this.toUpperCase())
        }

        enum class RelativeDirection(val transformTape: Tape.() -> Unit) {
            LEFT(Tape::moveLeft), RIGHT(Tape::moveRight)
        }
    }

    private data class State(val onZero: Instruction, val onOne: Instruction) {
        inline fun withValue(value: Int, action: Instruction.() -> Unit) = when (value) {
            0 -> action(onZero)
            1 -> action(onOne)
            else -> throw IllegalArgumentException("Illegal value")
        }
    }

    private class TuringMachine(
        var state: State,
        val states: Map<String, State>
    ) {
        private val tape = Tape()

        fun run(iterations: Int) = (0 until iterations).forEach { tick() }

        fun tick() = state.withValue(tape.currentValue) {
            tape.currentValue = this.value
            this.direction.transformTape(tape)
            state = states[this.next]!!
        }

        val checksum
            get() = tape.count { it == 1 }
    }

    private class Tape : Iterable<Int> {
        private var current = Node(value = 0)
        private var first = current
        private var last = current

        var currentValue: Int
            get() = current.value
            set(value) {
                current.value = value
            }

        fun moveLeft() {
            current = current.left ?: Node(right = current).also {
                current.left = it
                first = it
            }
        }

        fun moveRight() {
            current = current.right ?: Node(left = current).also {
                current.right = it
                last = it
            }
        }

        override fun iterator(): Iterator<Int> = object : Iterator<Int> {
            var iteratorValue: Node? = first

            override fun hasNext(): Boolean = iteratorValue != null

            override fun next(): Int =
                iteratorValue!!.value.also { iteratorValue = iteratorValue!!.right }
        }

        private data class Node(
            var value: Int = 0,
            var left: Node? = null,
            var right: Node? = null
        )
    }
}
