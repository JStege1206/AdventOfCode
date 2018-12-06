package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.*

class Day02 : Day(title = "Inventory Management System") {
    override fun first(input: Sequence<String>): Any = input
        .asSequence()
        .map { id -> id.groupingBy { it }.eachCount().values }
        .fold(Pair(0, 0)) { (twos, threes), counts ->
            Pair(
                if (2 in counts) twos + 1 else twos,
                if (3 in counts) threes + 1 else threes
            )
        }.let { (twos, threes) -> twos * threes }

    override fun second(input: Sequence<String>): Any = input
        .toList()
        .tails()
        .asSequence()
        .flatMap { c -> c.head.let { head -> c.tail.map { Pair(head, it) }.asSequence() } }
        .mapNotNull { (current, other) ->
            current.extractCommonCharacters(other).takeIf { it.length == current.length - 1 }
        }
        .first()

    private fun String.extractCommonCharacters(other: String): String =
        this.iterator().extractCommonCharacters(other.iterator())

    private fun CharIterator.extractCommonCharacters(other: CharIterator): String {
        tailrec fun extractCommon(
            t: Char,
            o: Char,
            diffs: Int,
            acc: StringBuilder
        ): StringBuilder =
            if (diffs > 1) acc
            else if (!this.hasNext() || !other.hasNext()) acc.applyIf({ t == o }) { append(t) }
            else if (t == o) extractCommon(this.next(), other.next(), diffs, acc.append(t))
            else extractCommon(this.next(), other.next(), diffs + 1, acc)

        return extractCommon(this.next(), other.next(), 0, StringBuilder()).toString()
    }
}
