package nl.jstege.adventofcode.aoccommon.days

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import nl.jstege.adventofcode.aoccommon.utils.Resource
import java.io.PrintStream

/**
 * Abstract implementation of a "Day" to be used for every Advent of Code assignment.
 * Each Day exists of two assignments ("first" and "second"), which are methods in this class.
 *
 * @author Jelle Stege
 *
 * @property title The title of the assignment.
 * @constructor Initialises this [Day] with an incomplete text. To run the assignments, use the
 * [Day.run] method.
 */
abstract class Day(private val title: String) {
    private lateinit var deferredFirst: Deferred<Pair<Any, Long>>
    private lateinit var deferredSecond: Deferred<Pair<Any, Long>>

    /**
     * The implementation for the first assignment of this [Day]
     *
     * @param input The input for this day, as a Sequence.
     * @return The result of this assignment.
     */
    abstract fun first(input: Sequence<String>): Any

    /**
     * The implementation for the first assignment of this [Day]
     *
     * @param input The input for this day, as a Sequence.
     * @return The result of this assignment.
     */
    abstract fun second(input: Sequence<String>): Any

    /**
     * Loads the input for this assignment and asynchronously executes the sub-assignments. Only
     * runs the assignments, does not check or wait for output.
     */
    fun run() {
        val input = loadInput()
        deferredFirst = GlobalScope.async { supplier { first(input) } }
        deferredSecond = GlobalScope.async { supplier { second(input) } }
    }

    /**
     * Supplier method to be executed asynchronous. Measures the time taken to execute the given
     * action with the given input.
     *
     * @param action The action to execute
     * @return Supplier to be completed by a task running in the fork join common pool.
     */
    private inline fun supplier(action: () -> Any): Pair<Any, Long> {
        val startTime = System.nanoTime()
        val output = action()
        val timeTaken = System.nanoTime() - startTime
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
     * Await until both assignments are done.
     */
    fun await() {
        runBlocking {
            deferredFirst.await()
            deferredSecond.await()
        }
    }
    
    /**
     * Writes the output, well formatted, to [ps].
     * @param ps PrintStream The [PrintStream] to use.
     */
    fun printOutputToWriter(ps: PrintStream = System.out) {
        ps.println("${this::class.java.simpleName}: $title")

        val (firstOutput, firstTime) = runBlocking { deferredFirst.await() }
        ps.println("Part One")
        ps.println("    Output: $firstOutput")
        ps.println("    Time taken: ${"%.2f".format(firstTime / 1000000F)}ms")

        val (secondOutput, secondTime) = runBlocking { deferredSecond.await() }
        ps.println("Part Two:")
        ps.println("    Output: $secondOutput")
        ps.println("    Time taken: ${"%.2f".format(secondTime / 1000000F)}ms")

    }

    override fun toString(): String = "${this::class.java.simpleName}: $title"
}

