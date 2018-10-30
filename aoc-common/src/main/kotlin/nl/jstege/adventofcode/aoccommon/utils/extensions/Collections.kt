package nl.jstege.adventofcode.aoccommon.utils.extensions

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
        this.transpose(mutableListOf())
    }
}

/**
 * Calculates all permutations of the given collection.
 *
 * @receiver The collection to calculate permutations of.
 * @return A sequence of all permutations.
 */
fun <E> Collection<E>.permutations(): Sequence<List<E>> = sequence {
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
fun <E> Collection<E>.combinations(n: Int): Sequence<Set<E>> {
    fun IntArray.valid(@Suppress("NAME_SHADOWING") n: Int, k: Int): Boolean {
        if (this.size != k) {
            return false
        }
        (0 until k).forEach { i ->
            if (this[i] < 0L || this[i] > n - 1) {
                return false
            }

            if ((i + 1 until k).filter { this[i] >= this[it] }.any()) {
                return false
            }
        }
        return true
    }

    return sequence {
        val dataSet = this@combinations.toList()
        val indices = IntArray(n) { it }
        while (indices.valid(dataSet.size, n)) {
            yield(indices.map { dataSet[it] }.toSet())
            var i = n - 1
            while (i > 0 && indices[i] == dataSet.size - n + i) {
                i--
            }
            indices[i]++

            while (i < n - 1) {
                indices[i + 1] = indices[i] + 1
                i++
            }
        }
    }
}


fun <E : Any> List<E>.cycle(): Sequence<E> {
    var i = 0
    return generateSequence { this[i++ % this.size] }
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

fun <E> List<E>.reverse(start: Int, length: Int): List<E> =
        (start until start + length)
                .zipWithReverse()
                .asSequence()
                .take(length / 2)
                .fold(this.toMutableList()) { list, (f, s) ->
                    list.swap(f % this.size, s % this.size)
                    list
                }
