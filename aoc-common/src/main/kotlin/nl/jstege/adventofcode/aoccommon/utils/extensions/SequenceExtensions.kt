package nl.jstege.adventofcode.aoccommon.utils.extensions

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

/**
 *
 * @author Jelle Stege
 */

/**
 * Applies an operation to a Sequence and emits each successive value.
 *
 * @param initial The initial value to be emitted, used as accumulative value for the operation.
 * @param operation The operation to invoke with the previously calculated value and the new value.
 * @return A new sequence with values calculated by the operation function.
 */
fun <T, R> Sequence<T>.scan(initial: R, operation: (acc: R, T) -> R): Sequence<R> {
    return ScanningSequence(this, initial, operation)
}

internal class ScanningSequence<out T, R> constructor(
        private val sequence: Sequence<T>,
        private val initial: R,
        private val operation: (acc: R, T) -> R) : Sequence<R> {

    override fun iterator(): Iterator<R> = object : Iterator<R> {
        val iterator = sequence.iterator()
        var previous = initial
        override fun next(): R {
            return previous.also { previous = operation(previous, iterator.next()) }
        }

        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

    }
}

operator fun <T> Sequence<T>.times(n: Int) = when (n) {
    0 -> emptySequence()
    else -> generateSequence { this }.take(n).flatten()
}

/**
 * Converts the first element in the sequence to a [JsonNode].
 *
 * @receiver A Sequence with a String in it.
 * @return The parsed [JsonNode] of the first element in the sequence.
 * @throws com.fasterxml.jackson.core.JsonParseException if the String can not be parsed to a
 * JsonNode.
 * @throws NoSuchElementException if the Sequence is empty.
 */
fun Sequence<String>.toJson(): JsonNode = ObjectMapper().readTree(this.first())!!

/**
 * Returns the first element of the Sequence
 *
 * @receiver The sequence to get the first element of.
 * @return The first element of the Sequence.
 */
inline val <E> Sequence<E>.head get() = this.first()


/**
 * Executes the given block in case there is no element in the given sequence.
 * @receiver The sequence to check
 * @param block The block to execute if there is no element present.
 * @return The result of the block function.
 */
inline fun <E> Sequence<E>.orElse(block: () -> E) = this.firstOrNull() ?: block()
