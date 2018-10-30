package nl.jstege.adventofcode.aoccommon.utils.extensions

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

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
 * Transforms all elements to a new destination.
 *
 * @receiver The iterable to transform.
 * @param destination The destination to transform all elements in the given iterable to
 * @param transform The transform function to.
 */
inline fun <T, R> Iterable<T>.transformTo(destination: R, transform: (R, T) -> Unit): R {
    this.forEach { transform(destination, it) }
    return destination
}

fun IntRange.zipWithReverse() = this.zip(this.reversed())

fun <A, B> Iterable<A>.parallelMap(f: suspend (A) -> B): List<B> = runBlocking {
    this@parallelMap.map { GlobalScope.async { f(it) } }.map { it.await() }
}

fun <T, R> Iterator<T>.map(transform: (T) -> R): Iterator<R> = object : Iterator<R> {
    override fun hasNext(): Boolean = this@map.hasNext()
    override fun next(): R = transform(this@map.next())
}
