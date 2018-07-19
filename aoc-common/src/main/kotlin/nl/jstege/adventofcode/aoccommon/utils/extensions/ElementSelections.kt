package nl.jstege.adventofcode.aoccommon.utils.extensions

import java.util.concurrent.ThreadLocalRandom

/**
 * Methods to select a certain element from a given list of arguments.
 * @author Jelle Stege
 */

/**
 * Returns the minimal element of the given arguments.
 *
 * @param elements The elements to find the minimum of.
 * @return The minimum in the given elements.
 */
fun <E : Comparable<E>> min(vararg elements: E): E = elements.min()!!

/**
 * Returns the maximum element of the given arguments.
 *
 * @param elements The elements to find the maximum of.
 * @return The maximum in the given elements.
 */
fun <E : Comparable<E>> max(vararg elements: E): E = elements.max()!!

/**
 * Returns the median element of the given arguments.
 *
 * @param elements The elements to find the median of.
 * @return The median in the given elements.
 */
fun <E : Comparable<E>> mid(vararg elements: E) = elements.toList().quickSelect(elements.size / 2)

private val RANDOM = ThreadLocalRandom.current()
/**
 * Finds the lowest kth element in the given List.
 *
 * @receiver The list to search for the lowest kth element.
 * @param index The index of the element to find.
 * @return The lowest kth element
 */
fun <E : Comparable<E>> List<E>.quickSelect(index: Int): E {
    fun <E : Comparable<E>> MutableList<E>.partition(left: Int, right: Int, pivotIndex: Int): Int {
        val pivot = this[pivotIndex]
        this.swap(pivotIndex, right)
        var storeIndex = left
        (left until right)
            .filter { this[it] < pivot }
            .forEach {
                this.swap(storeIndex, it)
                storeIndex++
            }
        this.swap(right, storeIndex)
        return storeIndex
    }

    tailrec fun <E : Comparable<E>> MutableList<E>.select(k: Int, left: Int, right: Int): E {
        if (left == right) {
            return this[left]
        }

        val pivotIndex = this@select.partition(left, right, RANDOM.nextInt(left, right))

        return when {
            k == pivotIndex -> this[k]
            k < pivotIndex -> this.select(k, left, pivotIndex - 1)
            else -> this.select(k, pivotIndex + 1, right)
        }
    }
    return this.toMutableList().select(index, 0, this.size - 1)
}
