package nl.jstege.adventofcode.aoccommon.utils.extensions

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.coroutines.experimental.buildSequence

/**
 * Extra utilities for lists.
 * @author Jelle Stege
 */

/**
 * Transposes a list of lists in 2 dimensional space.
 * @return The transposed list of lists.
 */
fun <E> List<List<E>>.transpose(): List<List<E>> {
    tailrec fun <E> List<List<E>>.transpose(accumulator: List<List<E>>): List<List<E>> =
            if (this.isEmpty() || this.first().isEmpty()) accumulator
            else this.map { it.tail }.transpose(accumulator + listOf(this.map { it.head }))

    return if (this.isEmpty() || this.head.isEmpty()) {
        throw IllegalArgumentException("Empty columns or rows found. Can not transpose.")
    } else {
        this.transpose(listOf())
    }
}

/**
 * Partitions a list into a list of sublists of a given size. If the chunk
 * size is not a divisor of the size of the list, the last chunk will
 * be smaller.
 *
 * @param size The size of the sublists.
 *
 * @return The partitioned list
 */
fun <E> List<E>.chunked(size: Int): List<List<E>> {
    tailrec fun <E> List<E>.chunked(@Suppress("NAME_SHADOWING") size: Int, acc: List<List<E>>): List<List<E>> =
            if (this.isEmpty()) acc
            else this.drop(size).chunked(size, acc + listOf(this.take(size)))
    return if (size < 1)
        throw IllegalArgumentException("Size too small")
    else this.chunked(size, listOf())
}

/**
 * Calculates all permutations of the given list.
 */
fun <E> Collection<E>.permutations(): Sequence<List<E>> {
    fun <E> MutableList<E>.swap(i: Int, j: Int) {
        val t = this[i]
        this[i] = this[j]
        this[j] = t
    }

    return buildSequence {
        val a = this@permutations.toMutableList()
        val c = IntArray(this@permutations.size) { 0 }
        yield(a.toList())

        var i = 0
        while (i < a.size) {
            if (c[i] < i) {
                if (i.isEven()) {
                    a.swap(0, i)
                } else {
                    a.swap(c[i], i)
                }
                yield(a.toList())
                c[i] += 1
                i = 0
            } else {
                c[i] = 0
                i += 1
            }
        }
    }
}

fun <E> Collection<E>.combinations(n: Int): Sequence<List<E>> {
    fun IntArray.validCombination(@Suppress("NAME_SHADOWING") n: Int, k: Int): Boolean {
        if (this.size != k) {
            return false
        }
        (0 until k).forEach { i ->
            if (this[i] < 0L || this[i] > n - 1) {
                return false
            }

            (i + 1 until k)
                    .filter { this[i] >= this[it] }
                    .onFirst { return false }
        }
        return true
    }

    return buildSequence {
        val dataSet = this@combinations.toList()
        val combination = IntArray(n) { it }
        while (combination.validCombination(dataSet.size, n)) {
            yield(combination.map { dataSet[it] })
            var i = n - 1
            while (i > 0 && combination[i] == dataSet.size - n + i) {
                i--
            }
            combination[i]++

            while (i < n - 1) {
                combination[i + 1] = combination[i] + 1
                i++
            }
        }
    }
}

operator fun <E> java.util.ArrayDeque<E>.plusAssign(v: E) {
    this.add(v)
}

inline fun <T> Iterable<T>.sumBy(selector: (T) -> Long): Long = this
        .fold(0L) { sum, el -> sum + selector(el) }


fun List<Int>.takeWhileSumGreaterThan(n: Int): List<Int> {
    var sum = 0
    return this.takeWhile {
        sum += it
        sum > n
    }
}

fun List<Int>.takeWhileSumLessThan(n: Int): List<Int> {
    var sum = 0
    return this.takeWhile {
        sum += it
        sum < n
    }
}


inline fun <T, R> Sequence<T>.foldWhile(predicate: (R, T) -> Boolean,
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

fun ByteArray.prefixedWithZeroes(amount: Int): Boolean {
    val checks = amount / 2
    val zero = 0.toByte()
    if (this.size < checks) return false
    (0 until checks)
            .filter { this[it] != zero }
            .onFirst { return false }
    return amount.isEven() || (this.size >= checks + 1 && this[checks].toInt() and 0xF0 == 0)
}

inline fun <E> Iterable<E>.onFirst(action: () -> Unit) {
    if (this.firstOrNull() != null) {
        action()
    }
}

inline fun <E> Sequence<E>.onFirst(action: () -> Unit) {
    if (this.firstOrNull() != null) {
        action()
    }
}

operator fun <E> List<E>.component6() = this[5]
operator fun <E> List<E>.component7() = this[6]
operator fun <E> List<E>.component8() = this[7]
inline val <E> Collection<E>.tail get() = this.drop(1)
inline val <E> Collection<E>.head get() = this.first()
inline val <E> Collection<E>.last get() = this.last()
inline val <E> Collection<E>.init get() = this.take(this.size - 1)
inline val <E> Sequence<E>.head get() = this.first()
inline val <E> Sequence<E>.tail get() = this.drop(1)

fun Sequence<String>.toJson(): JsonNode = ObjectMapper().readTree(this.first())!!

operator fun <K, L, V> Map<K, Map<L, V>>.get(k1: K, k2: L) = this[k1]?.get(k2)
operator fun <K, L, V> MutableMap<K, MutableMap<L, V>>.set(k1: K, k2: L, v: V): V? = this
        .getOrPut(k1, { mutableMapOf() }).put(k2, v)

inline fun <E> Sequence<E>.firstOrElse(block: () -> E) = this.firstOrNull() ?: block()
inline fun <E> Sequence<E>.firstOrElse(predicate: (E) -> Boolean, block: () -> E): E {
    for (element in this) if (predicate(element)) return element
    return block()
}