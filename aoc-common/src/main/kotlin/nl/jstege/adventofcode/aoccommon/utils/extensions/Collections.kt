package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 * Extra utilities for lists.
 * @author Jelle Stege
 */

/**
 * Transposes a collection of collections. So:
 * |1 2 3|         |1 4 7|
 * |4 5 6| becomes |2 5 8|
 * |7 8 9|         |3 6 9|
 *
 * Note; this function is a recursive operation.
 *
 * @return The transposed collection of collections.
 */
tailrec fun <E> List<List<E>>.transpose(acc: List<List<E>> = emptyList()): List<List<E>> =
        when (this.isEmpty() || this.first().isEmpty()) {
            true -> acc
            else -> this.map { it.drop(1) }.transpose(acc + listOf(this.map { it.first() }))
        }


/**
 * Partitions a list into a list of sublists of a given size. If the partition
 * size is not a divisor of the size of the list, the last partition will
 * be smaller.
 *
 * @param size The size of the sublists.
 *
 * @return The partitioned list
 */
tailrec fun <E> List<E>.partition(size: Int, acc: List<List<E>> = listOf()): List<List<E>> =
        when (this.size) {
            0 -> acc
            else -> this.drop(size).partition(size, acc + listOf(this.take(size)))
        }

/**
 * Calculates all permutations of the given list.
 */
fun <E> List<E>.permutations(): List<List<E>> = (0 until this.size.factorial())
        .fold(mutableListOf<List<E>>(), { r, i ->
            r += this.toMutableList().permutation(i)
            r
        })


private tailrec fun <E> MutableList<E>.permutation(no: Long, acc: List<E> = listOf()): List<E> =
        when (this.size) {
            0 -> acc
            else -> {
                val subFac = (this.size - 1).factorial()
                this.permutation(no % subFac, acc + this.removeAt((no / subFac).toInt()))
            }
        }


@Suppress("NOTHING_TO_INLINE")
inline operator fun <E> java.util.ArrayDeque<E>.plusAssign(v: E) {
    this.add(v)
}

inline fun <T> Iterable<T>.sumBy(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}


fun ByteArray.prefixedWithZeroes(amount: Int): Boolean {
    val checks = amount / 2
    val zero = 0.toByte()
    if (this.size < checks) return false
    (0 until checks)
            .filter { this[it] != zero }
            .any { return false }
    return !(amount.isOdd() && (this.size < checks + 1) || ((this[checks].toInt() and 0xF0 != 0)))
}

operator fun <E> List<E>.component6() = this[5]
inline val <E> Collection<E>.tail get() = this.drop(1)
inline val <E> Collection<E>.head get() = this.first()
inline val <E> Collection<E>.last get() = this.last()
inline val <E> Collection<E>.init get() = this.take(this.size - 1)
inline val <E> Sequence<E>.head get() = this.first()
inline val <E> Sequence<E>.tail get() = this.drop(1)

fun Sequence<String>.toJson(): com.fasterxml.jackson.databind.JsonNode = com.fasterxml.jackson.databind.ObjectMapper().readTree(this.first())!!

operator fun <K, L, V> Map<K, Map<L, V>>.get(k1: K, k2: L) = this[k1]?.get(k2)
operator fun <K, L, V> MutableMap<K, MutableMap<L, V>>.set(k1: K, k2: L, v: V): V? = this
        .getOrPut(k1, { mutableMapOf() }).put(k2, v)
