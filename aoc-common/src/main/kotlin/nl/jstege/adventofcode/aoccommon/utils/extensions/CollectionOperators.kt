package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 * Extension methods on collections.
 * @author Jelle Stege
 */

/**
 * Allows a get operation with 2 keys on a map of maps. This gives some sort of 3D-hashtable
 * implementation a nice get method.
 *
 * @receiver The 3D-hashtable-like structure.
 * @param k1 The first key
 * @param k2 The second key
 * @return The value belonging to (k1, k2) or null if it does not exist.
 */
operator fun <K, L, V> Map<K, Map<L, V>>.get(k1: K, k2: L) = this[k1]?.get(k2)

/**
 * Allows a set operation with 2 keys on a map of maps. This gives some sort of 3D-hashtable
 * implementation a nice set method.
 *
 * @receiver The 3D-hashtable-like structure.
 * @param k1 The first key
 * @param k2 The second key
 * @param v The value to set
 * @return The previous value associated with the given key-pair or null if there was no previous
 *  value.
 */
operator fun <K, L, V> MutableMap<K, MutableMap<L, V>>.set(k1: K, k2: L, v: V): V? = this
        .getOrPut(k1, { mutableMapOf() }).put(k2, v)

/**
 * Returns the 5th element of the given list.
 *
 * @receiver The list to use.
 * @return The 5th element of the list.
 */
operator fun <E> List<E>.component6() = this[5]

/**
 * Returns the 6th element of the given list.
 *
 * @receiver The list to use.
 * @return The 6th element of the list.
 */

operator fun <E> List<E>.component7() = this[6]

/**
 * Returns the 7th element of the given list.
 *
 * @receiver The list to use.
 * @return The 7th element of the list.
 */
operator fun <E> List<E>.component8() = this[7]

/**
 * Returns the tail, or everything but the first element, of the given collection.
 *
 * @receiver The collection.
 * @return A copy of the collection, minus the first element.
 */
inline val <E> Collection<E>.tail get() = this.drop(1)

/**
 * Returns the head, or first element, of the given collection.
 *
 * @receiver The collection.
 * @return The first element of the collection.
 * @throws NoSuchElementException if there is no head.
 */
inline val <E> Collection<E>.head get() = this.first()

/**
 * Returns the last element of the given collection.
 *
 * @receiver The collection.
 * @return The last element of the collection.
 * @throws NoSuchElementException if there is no last element e.g. the list is empty..
 */
inline val <E> Collection<E>.last get() = this.last()

/**
 * Returns the init, or everything but the last element, of the collection.
 *
 * @receiver The collection.
 * @return The init of the collection.
 */
inline val <E> Collection<E>.init get() = this.take(this.size - 1)
