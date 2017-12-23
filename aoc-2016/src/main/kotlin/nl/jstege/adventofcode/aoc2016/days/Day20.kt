package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.sumBy

/**
 *
 * @author Jelle Stege
 */
class Day20 : Day() {
    private companion object Configuration {
        private const val MAX_ADDRESS = (1L shl Integer.SIZE) - 1
    }

    override val title: String = "Firewall Rules"

    override fun first(input: Sequence<String>): Any = input
            .map(Day20::IpRange).optimize().first
            .first().end + 1

    override fun second(input: Sequence<String>): Any = input
            .map(Day20::IpRange).optimize().second
            .sumBy { it.end + 1 - it.start }

    private fun Sequence<IpRange>.optimize(): Pair<List<IpRange>, List<IpRange>> {
        val blockedRanges = mutableListOf<IpRange>()
        val allowedRanges = mutableListOf<IpRange>()
        val trackedHigh = this
                .sortedBy { it.start }
                .fold(Pair(0L, 0L), { (trackedHigh, trackedLow), (start, end) ->
                    if (start > trackedHigh + 1) {
                        blockedRanges += IpRange(trackedLow, trackedHigh)
                        allowedRanges += IpRange(trackedHigh + 1, start - 1)
                        Pair(end, start)
                    } else {
                        Pair(Math.max(trackedHigh, end), trackedLow)
                    }
                }).first
        if (trackedHigh < MAX_ADDRESS) {
            allowedRanges += IpRange(trackedHigh + 1, MAX_ADDRESS)
        }
        return Pair(blockedRanges, allowedRanges)
    }

    private data class IpRange(val start: Long, val end: Long) {
        constructor(i: List<Long>) : this(i[0], i[1])
        constructor(s: String) : this(s.split('-').map(String::toLong).sorted())
    }
}
