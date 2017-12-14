package nl.jstege.adventofcode.aoccommon.days


import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import nl.jstege.adventofcode.aoccommon.utils.Resource
import kotlin.system.measureNanoTime

/**
 * Abstract implementation of a "Day" to be used for every Advent of Code assignment.
 * Each Day exists of two assignments ("first" and "second"), which are methods in this class.
 *
 * @author Jelle Stege
 *
 * @constructor Initialises this [Day] with an incomplete text. To run the assignments, use the
 * [Day.run] method.
 */
abstract class Day {
    private lateinit var deferredFirst: Deferred<Pair<Any, Long>>
    private lateinit var deferredSecond: Deferred<Pair<Any, Long>>

    /**
     * The implementation for the first assignment of this [Day]
     *
     * @param input The input for this day, as a Sequence.
     * @return The result of this assignment.
     */
    abstract suspend fun first(input: Sequence<String>): Any

    /**
     * The implementation for the first assignment of this [Day]
     *
     * @param input The input for this day, as a Sequence.
     * @return The result of this assignment.
     */
    abstract suspend fun second(input: Sequence<String>): Any

    /**
     * Loads the input for this assignment and asynchronously executes the sub-assignments. Only
     * runs the assignments, does not check or wait for output.
     */
    fun run() {
        val input = loadInput()
        deferredFirst = runBlocking { async(CommonPool) { supplier({ first(input) }) } }
        deferredSecond = runBlocking { async(CommonPool) { supplier({ second(input) }) } }
    }

    /**
     * Supplier method to be executed asynchronous. Measures the time taken to execute the given
     * action with the given input.
     *
     * @param action The action to execute
     * @return Supplier to be completed by a task running in the fork join common pool.
     */
    private suspend fun supplier(action: suspend () -> Any): Pair<Any, Long> {
        var output: Any = object {}
        val timeTaken = measureNanoTime {
            output = action()
        }
        return Pair(output, timeTaken)
    }

    /**
     * Loads the input corresponding to the current day implementation
     *
     * @return The input corresponding to the day implementation.
     */
    fun loadInput() =
            Resource.readLinesAsSequence("input/${this::class.java.simpleName.toLowerCase()}.txt")

    /**
     * Returns the String representation when the output is available. This function will suspend
     * until the assignments are complete.
     *
     * @return The output, formatted.
     */
    override fun toString(): String {
        val (firstOutput, firstTime) = runBlocking { deferredFirst.await() }
        val (secondOutput, secondTime) = runBlocking { deferredSecond.await() }
        return StringBuilder().append("${this::class.java.simpleName}\n")
                .appendln("First:")
                .appendln("    Output: $firstOutput")
                .appendln("    Time taken: %.2fms".format(firstTime / 1000000F))
                .appendln("Second:")
                .appendln("    Output: $secondOutput")
                .appendln("    Time taken: %.2fms".format(secondTime / 1000000F))
                .toString()
    }
}
