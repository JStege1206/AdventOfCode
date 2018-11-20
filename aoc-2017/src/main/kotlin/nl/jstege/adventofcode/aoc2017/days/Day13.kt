package nl.jstege.adventofcode.aoc2017.days

import nl.jstege.adventofcode.aoccommon.days.Day

class Day13 : Day(title = "Packet Scanners") {
    override fun first(input: Sequence<String>): Any {
        return input.parse().sumBy { (d, r) -> if (d % (2 * r - 2) == 0) d * r else 0 }
    }

    override fun second(input: Sequence<String>): Any {
        return input.parse().toList()
            .let { s ->
                (0..Int.MAX_VALUE).first { s.none { (d, r) -> (d + it) % (2 * r - 2) == 0 } }
            }
    }

    private fun Sequence<String>.parse() = this.map { line -> line.split(": ").map { it.toInt() } }
}
