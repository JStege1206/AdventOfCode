package nl.jstege.adventofcode.aoccommon.utils.extensions

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

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
 * Scan is similar to [Iterable.fold], but returns a list of successive reduced values.
 *
 * @receiver The List to accumulate.
 * @param initial The initial value
 * @param operation The operation used to reduced values
 * @return a list of successive reduced values.
 */
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
    if (iterator().hasNext()) {
        action()
    }
}

fun IntRange.zipWithReverse() = this.zip(this.reversed())

fun <A, B> Iterable<A>.parallelMap(f: suspend (A) -> B): List<B> = runBlocking {
    map { async(CommonPool) { f(it) } }.map { it.await() }
}
