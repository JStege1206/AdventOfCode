package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 * Extention methods for iterables.
 * @author Jelle Stege
 */

/**
 * Sums the iterable by a given selector as a Long.
 *
 * @receiver The iterable to iterate over.
 * @param selector The selector to use.
 * @returns The sum of the iterable by the given selector.
 */
inline fun <T> Iterable<T>.sumBy(selector: (T) -> Long): Long = this
        .fold(0L) { sum, el -> sum + selector(el) }

inline fun <T, R> Iterable<T>.scan(initial: R, operation: (R, T) -> R): List<R> {
    val result = mutableListOf(initial)
    var acc = initial
    for (el in this) {
        acc = operation(acc, el)
        result += acc
    }
    return result
}

/**
 * Runs the supplied action if there is at least one element in the iterable.
 *
 * @receiver The iterable.
 * @param action The action to execute if there is at least one element in the iterable.
 */
inline fun <E> Iterable<E>.ifPresent(action: () -> Unit) {
    if (this.firstOrNull() != null) {
        action()
    }
}
