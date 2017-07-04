package nl.jstege.adventofcode.aoccommon.utils.extensions

import kotlin.coroutines.experimental.buildSequence

/**
 * Extra utilities for lists.
 * @author Jelle Stege
 */

/**
 * Transposes a list of lists in 2 dimensional space. This function does not check whether all
 * rows have an equal amount of elements. Rather, it assumes all rows have an equal amount of
 * elements to the first row. Giving a 2D-list of varying row sizes results in undefined behaviour.
 *
 * @receiver The list to transpose.
 * @return The transposed list of lists.
 * @throws IllegalArgumentException if the list is empty, or the columns are empty.
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
 * Partitions a list into a list of chunks of a given size. If the chunk
 * size is not a divisor of the size of the list, the last chunk will
 * be smaller.
 *
 * @receiver The list to cut into chunks.
 * @param size The size of the chunks.
 * @return The partitioned list
 * @throws IllegalArgumentException if the size of the chunks is less than 1.
 */
fun <E> List<E>.chunked(size: Int): List<List<E>> {
    tailrec fun <E> List<E>.chunked(@Suppress("NAME_SHADOWING") size: Int,
                                    accumulator: List<List<E>>): List<List<E>> =
            if (this.isEmpty()) accumulator
            else this.drop(size).chunked(size, accumulator + listOf(this.take(size)))

    return if (size < 1)
        throw IllegalArgumentException("Size too small")
    else this.chunked(size, listOf())
}

/**
 * Calculates all permutations of the given collection.
 *
 * @receiver The collection to calculate permutations of.
 * @return A sequence of all permutations.
 */
fun <E> Collection<E>.permutations(): Sequence<List<E>> = buildSequence {
    val a = this@permutations.toMutableList()
    val c = IntArray(this@permutations.size) { 0 }
    yield(a.toList())

    var i = 0
    while (i < a.size) {
        if (c[i] < i) {
            a.swap(if (i.isEven()) 0 else c[i], i)

            yield(a.toList())
            c[i] += 1
            i = 0
        } else {
            c[i] = 0
            i += 1
        }
    }
}

/**
 * Calculates and returns all combinations of a given size of the given collection.
 *
 * @receiver The collection to calculate combinations of.
 * @param n The size of the combinations.
 * @return A sequence of all combinations.
 */
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
                    .ifPresent { return false }
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

/**
 * Swaps two elements in the given MutableList
 *
 * @receiver The MutableList to mutate.
 * @param i The index of the first element.
 * @param j The index of the second element.
 */
fun <E> MutableList<E>.swap(i: Int, j: Int) {
    val t = this[i]
    this[i] = this[j]
    this[j] = t
}