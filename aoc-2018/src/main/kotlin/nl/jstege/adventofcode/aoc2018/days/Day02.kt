package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.tail
import nl.jstege.adventofcode.aoccommon.utils.extensions.tails

class Day02 : Day(title = "Inventory Management System") {
    override fun first(input: Sequence<String>): Any {
        return input
            .fold(Pair(0, 0)) { (twos, threes), it ->
                Pair(
                    if (it.containsN(2)) twos + 1 else twos,
                    if (it.containsN(3)) threes + 1 else threes
                )
            }
            .let { (twos, threes) -> twos * threes }
    }

    override fun second(input: Sequence<String>): Any {
        return input
            .toList()
            .tails()
            .asSequence()
            .mapNotNull { ids ->
                val currentId = ids.head
                ids.tail.asSequence()
                    .map { currentId.keepCommon(it) }
                    .firstOrNull { it.length == currentId.length - 1 }
            }
            .first { it.isNotEmpty() }
    }

    private fun String.containsN(n: Int): Boolean = this
        .groupBy { it }
        .values
        .any { it.size == n }

    private fun String.keepCommon(s: String): String {
        val r = StringBuilder()
        var differences = 0
        for (i in 0 until length) {
            if (this[i] != s[i]) {
                differences += 1
            } else {
                r.append(this[i])
            }
            if (differences > 1) {
                break
            }
        }
        return r.toString()
    }
}
