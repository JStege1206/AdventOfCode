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


/**
 * Takes elements of the iterable while the sum of the taken elements is bigger than the given n.
 *
 * @receiver The iterable to take elements from.
 * @param n the limit used.
 * @return The taken elements.
 */
fun Iterable<Int>.takeWhileSumGreaterThan(n: Int): List<Int> {
    var sum = 0
    return this.takeWhile {
        sum += it
        sum > n
    }
}

/**
 * Takes elements of the iterable while the sum of the taken elements is less than the given n.
 *
 * @receiver The iterable to take elements from.
 * @param n the limit used.
 * @return The taken elements.
 */
fun Iterable<Int>.takeWhileSumLessThan(n: Int): List<Int> {
    var sum = 0
    return this.takeWhile {
        sum += it
        sum < n
    }
}

/**
 * Folds the Iterable while the given predicate is true. If the predicate evaluates to false,
 * the folding stops and the result up until then is returned.
 *
 * @receiver The iterable to fold.
 * @param predicate The predicate to use, receives the accumulator as first argument and the
 * current element as second.
 * @param initial The initial value used as accumulator.
 * @param operation The operation used to fold the current element to the accumulator.
 * @return The folded accumulator.
 */
inline fun <T, R> Iterable<T>.foldWhile(predicate: (R, T) -> Boolean,
                                        initial: R, operation: (acc: R, T) -> R): R {
    var accumulator = initial
    for (element in this) {
        if (!predicate(accumulator, element)) {
            return accumulator
        }
        accumulator = operation(accumulator, element)
    }
    return accumulator
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