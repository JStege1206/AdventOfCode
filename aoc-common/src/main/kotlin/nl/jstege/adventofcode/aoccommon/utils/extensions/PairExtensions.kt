package nl.jstege.adventofcode.aoccommon.utils.extensions

infix fun <E : Comparable<E>> E.sortTo(other: E): Pair<E, E> {
    val min = min(this, other)
    val max = max(this, other)
    return min to max
}


fun <S, T: S> Pair<T, T>.reduce(operation: (first: S, T) -> S): S =
        operation(this.first, this.second)
