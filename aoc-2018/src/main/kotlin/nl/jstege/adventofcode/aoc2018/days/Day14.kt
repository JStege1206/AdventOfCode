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
        return generateRecipes()
            .asSequence()
            .drop(input.head.toInt())
            .take(10)
            .joinToString("")
    }

    override fun second(input: Sequence<String>): Any {
        val pattern = input.head
        val target = input.head.toLong()
        val mod = pow(10, pattern.length.toLong())
        println(mod)
        return generateRecipes()
            .asSequence()
            .scan(0L to 0) { (current, i), v ->
                var c = (10 * current + v)
                while (c >= mod) c -= mod
                c to i + 1
            }
            .first { it.first == target }
            .let { (_, i) -> i - pattern.length }
    }


    private tailrec fun pow(a: Long, b: Long, acc: Long = 1): Long = when {
        b == 0L -> acc
        b.isEven -> pow(a * a, b ushr 1, acc)
        else -> pow(a * a, b ushr 1, acc * a)
    }

    private fun generateRecipes(): Iterator<Int> = object : Iterator<Int> {
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

                i += 1 + scores[i]
                while (i >= scores.size) i -= scores.size
                j += 1 + scores[j]
                while (j >= scores.size) j -= scores.size
                return 1
            }

            scores.add(x)

            i += 1 + scores[i]
            while (i >= scores.size) i -= scores.size
            j += 1 + scores[j]
            while (j >= scores.size) j -= scores.size

            return x
        }
    }
}
