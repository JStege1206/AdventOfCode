package nl.jstege.adventofcode.aoccommon.utils.extensions

/**
 * Util functions
 * @author Jelle Stege
 */

/**
 * Applies an operation to a Sequence and emits each successive value. Takes the first element as
 * it's initial value.
 *
 * @param operation The operation to invoke with the previously calculated value and the new value.
 * @return A new sequence with values calculated by the operation function.
 */
fun <T> Sequence<T>.scan(operation: (acc: T, T) -> T): Sequence<T> = this.iterator()
    .let { ScanningSequence(it, it.next(), operation) }

/**
 * Applies an operation to a Sequence and emits each successive value.
 *
 * @param initial The initial value to be emitted, used as accumulative value for the operation.
 * @param operation The operation to invoke with the previously calculated value and the new value.
 * @return A new sequence with values calculated by the operation function.
 */
fun <T, R> Sequence<T>.scan(initial: R, operation: (acc: R, T) -> R): Sequence<R> =
    ScanningSequence(this, initial, operation)

/**
 * Transforms all elements to a new destination.
 *
 * @receiver The sequence to transform.
 * @param destination The destination to transform all elements in the given sequence to
 * @param transform The transform function to.
 */
fun <T, R> Sequence<T>.transformTo(destination: R, transform: (R, T) -> Unit): R =
    destination.apply { this@transformTo.forEach { transform(this, it) } }

private class ScanningSequence<out T, R> constructor(
    private val iterator: Iterator<T>,
    private val initial: R,
    private val operation: (acc: R, T) -> R
) : Sequence<R> {
    constructor(
        sequence: Sequence<T>,
        initial: R,
        operation: (acc: R, T) -> R
    ) : this(sequence.iterator(), initial, operation)

    override fun iterator(): Iterator<R> = object : Iterator<R> {
        var previous = initial

        override fun next(): R = previous.also { previous = operation(previous, iterator.next()) }

        override fun hasNext(): Boolean = iterator.hasNext()
    }
}

operator fun <T> Sequence<T>.times(n: Int) = generateSequence { this }.take(n).flatten()

/**
 * Returns the first element of the Sequence
 *
 * @receiver The sequence to get the first element of.
 * @return The first element of the Sequence.
 */
inline val <E> Sequence<E>.head get() = this.first()
