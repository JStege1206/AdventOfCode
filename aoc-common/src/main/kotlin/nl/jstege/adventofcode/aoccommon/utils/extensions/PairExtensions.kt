package nl.jstege.adventofcode.aoccommon.utils.extensions

infix fun <E : Comparable<E>> E.sortTo(other: E): Pair<E, E> {
    val min = min(this, other)
    val max = max(this, other)
    return min to max
}
