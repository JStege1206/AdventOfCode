package nl.jstege.adventofcode.aoccommon.utils.extensions

import java.util.*

/**
 * Extension methods for the ArrayDeque class.
 * @author Jelle Stege
 */

/**
 * Adds an element to the end of the [ArrayDeque].
 * @see java.util.ArrayDeque.add
 *
 * @receiver The Deque to use
 * @param v The value to add to the end of the Deque.
 */
operator fun <E> ArrayDeque<E>.plusAssign(v: E) {
    this.add(v)
}
