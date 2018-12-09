package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head

class Day08 : Day(title = "") {
    override fun first(input: Sequence<String>): Any =
        input.head
            .parse()
            .buildTree()
            .walk()
            .sumBy { it.metadata.sum() }

    override fun second(input: Sequence<String>): Any =
        input.head
            .parse()
            .buildTree().value

    fun String.parse(): Iterator<Int> = object : Iterator<Int> {
        val iterator = this@parse.iterator()
        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): Int {
            var n = 0
            var c = iterator.nextChar()
            do {
                n = n * 10 + (c - '0')
                if (iterator.hasNext()) c = iterator.nextChar()
            } while (c != ' ' && iterator.hasNext())

            return n
        }
    }
    
    private fun Iterator<Int>.buildTree(): Node = (next() to next())
        .let { (children, metadata) ->
            Node(
                (0 until children).map { buildTree() },
                (0 until metadata).map { next() }
            )
        }

    private data class Node(val children: List<Node>, val metadata: List<Int>) {
        val value: Int
            get() =
                if (children.isEmpty()) metadata.sum()
                else metadata.sumBy { if (it - 1 < children.size) children[it - 1].value else 0 }

        fun walk(): Sequence<Node> =
            sequenceOf(this) + this.children.asSequence().flatMap { it.walk() }
    }
}
