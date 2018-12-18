package nl.jstege.adventofcode.aoc2018.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head
import nl.jstege.adventofcode.aoccommon.utils.extensions.isEven
import nl.jstege.adventofcode.aoccommon.utils.extensions.scan
import java.util.*

class Day14 : Day(title = "Chocolate Charts") {
    private companion object {
        private val INITIAL = arrayOf(3, 7)
    }

    override fun first(input: Sequence<String>): Any {
        return RecipeSequence()
            .drop(input.head.toInt())
            .take(10)
            .joinToString("")
    }

    override fun second(input: Sequence<String>): Any {
        val pattern = input.head
        val target = input.head.toLong()
        val mod = pow(10, pattern.length.toLong())
        return RecipeSequence()
            .scan(0L to 0) { (current, i), v ->
                (10 * current + v) % mod to i + 1
            }
            .first { it.first == target }
            .let { (_, i) -> i - pattern.length }
    }


    private tailrec fun pow(a: Long, b: Long, acc: Long = 1): Long = when {
        b == 0L -> acc
        b.isEven -> pow(a * a, b ushr 1, acc)
        else -> pow(a * a, b ushr 1, acc * a)
    }

    class RecipeSequence() : Sequence<Int> {
        override fun iterator(): Iterator<Int> = object : Iterator<Int> {
            var scores = mutableListOf(*INITIAL)
            var remaining = ArrayDeque<Int>(scores)
            var i = 0
            var j = 1

            override fun hasNext(): Boolean = true

            override fun next(): Int {
                if (remaining.isNotEmpty()) {
                    return remaining.removeFirst()
                }

                val x = scores[i] + scores[j]

                if (x >= 10) {
                    scores.add(1)
                    scores.add(x - 10)
                    remaining.add(x - 10)

                    i = (i + 1 + scores[i]) % scores.size
                    j = (j + 1 + scores[j]) % scores.size
                    return 1
                }

                scores.add(x)

                i = (i + 1 + scores[i]) % scores.size
                j = (j + 1 + scores[j]) % scores.size

                return x
            }
        }

    }
}
