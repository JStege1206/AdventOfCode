package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 *
 * @author Jelle Stege
 */
infix fun <T> Comparable<T>.greaterThan(o: T) = this > o

infix fun <T> Comparable<T>.lessThan(o: T) = this < o
infix fun <T> Comparable<T>.greaterThanEquals(o: T) = this >= o
infix fun <T> Comparable<T>.lessThanEquals(o: T) = this <= o
infix fun <T> Comparable<T>.notEquals(o: T) = this != o
