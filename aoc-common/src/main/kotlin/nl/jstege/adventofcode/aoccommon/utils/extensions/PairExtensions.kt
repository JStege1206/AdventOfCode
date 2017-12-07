package nl.jstege.adventofcode.aoccommon.utils.extensions

infix fun <E : Comparable<E>> E.sortTo(other: E): Pair<E, E> = min(this, other) to max(this, other)
