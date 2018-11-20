package nl.jstege.adventofcode.aoccommon.days

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import nl.jstege.adventofcode.aoccommon.utils.Resource
import java.io.File
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
    private val inputs = mutableMapOf<String, Pair<Deferred<Solution>, Deferred<Solution>>>()

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
     * Loads all inputs corresponding to the current day implementation.
     *
     * @return The input corresponding to the day implementation.
     */
    fun loadInputFiles() = Resource
        .getResourceFiles(
            "input/${this::class.java.simpleName.substringAfter("Day").toLowerCase()}"
        )
        .map { Pair(it, Resource.readLinesAsSequence(it)) }

    /**
     * Loads all inputs for this assignment and asynchronously executes the sub-assignments. Only
     * runs the assignments, does not check or wait for output.
     */
    fun run() {
        loadInputFiles().forEach { (file, input) ->
            inputs[file] = Pair(
                GlobalScope.async { supplier { first(input) } },
                GlobalScope.async { supplier { second(input) } }
            )
        }
    }

    /**
     * Supplier method to be executed asynchronous. Measures the time taken to execute the given
     * action with the given input.
     *
     * @param action The action to execute
     * @return Supplier to be completed by a task running in the fork join common pool.
     */
    private inline fun supplier(action: () -> Any): Solution {
        val startTime = System.nanoTime()
        val output = action()
        val timeTaken = System.nanoTime() - startTime
        return Pair(output, timeTaken)
    }


    /**
     * Writes the output, well formatted, to [ps].
     * @param ps PrintStream The [PrintStream] to use.
     */
    fun awaitAndPrintOutput(ps: PrintStream = System.out) {
        ps.println(this)

        inputs.forEach { (file, runners) ->
            val (firstOutput, firstTime) = runBlocking { runners.first.await() }
            ps.println("Input file: ${file.substringAfterLast(File.separator)}")
            ps.println("    Part One")
            ps.println("        Output: $firstOutput")
            ps.printf("        Time taken: %.2fms\n", firstTime / 1000000F)

            val (secondOutput, secondTime) = runBlocking { runners.second.await() }
            ps.println("    Part Two:")
            ps.println("        Output: $secondOutput")
            ps.printf("        Time taken: %.2fms\n", secondTime / 1000000F)
        }
    }

    override fun toString(): String = "${this::class.java.simpleName}: $title"
}
