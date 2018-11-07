package nl.jstege.adventofcode.aoccommon.utils

import kotlinx.coroutines.runBlocking
import nl.jstege.adventofcode.aoccommon.days.Day
import org.junit.Assert
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Executes both the first and second method on the given [Day] and checks whether the output
 * is equal to the expected output (Should be in a file "output/<Day classname in lowercase>.txt"
 * on the classpath).
 * @author Jelle Stege
 * @property day The day to test.
 * @constructor Initializes the [DayTester] with a [Day] instance.
 */
abstract class DayTester(val day: Day) {
    /**
     * The inputs belonging to [day].
     */
    private val inputs = day.loadInputFiles().map { (file, input) ->
        Pair(file.substringAfterLast(File.separator), input)
    }
    /**
     * The outputs belonging to [day].
     */
    private val output = loadOutputs(day::class.java)

    /**
     * Tests the first assignment of [day].
     */
    @Test
    fun testFirst() {
        try {
            inputs.forEach { (file, input) ->
                val actualOutput = runBlocking { day.first(input).toString() }
                val expectedOutput = output[file]?.get(0) ?: Assert.fail("Output not present.")
                assertEquals(expectedOutput, actualOutput)
            }
        } catch (e: NotImplementedError) {
            //empty, don't fail on a to-do.
        }
    }

    /**
     * Tests the second assignment of [day].
     */
    @Test
    fun testSecond() {
        try {
            inputs.forEach { (file, input) ->
                val actualOutput = runBlocking { day.second(input).toString() }
                val expectedOutput = output[file]?.get(1) ?: Assert.fail("Output not present.")
                assertEquals(expectedOutput, actualOutput)
            }
        } catch (e: NotImplementedError) {
            //empty, don't fail on a to-do
        }
    }

    companion object {
        /**
         * Loads all outputs for [day]. Should be in files corresponding the input files; thus
         * "output/<Day classname, without "Day" prepended>/<input file name>.txt" on the classpath,
         * where N is the name of the input file.
         * @param c The [Class] belonging to the [day].
         * @return A map of file names to outputs, having the solution for this [day]
         */
        private fun loadOutputs(c: Class<out Day>) = Resource
            .getResourceFiles("output/${c.simpleName.substringAfter("Day").toLowerCase()}/")
            .associate {
                Pair(
                    it.substringAfterLast(File.separator),
                    Resource.readLinesAsSequence(it).toList()
                )
            }
    }
}
