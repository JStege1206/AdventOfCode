package nl.jstege.adventofcode.aoccommon

/**
 * AdventOfCode implementation
 *
 * @author Jelle Stege
 */
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import nl.jstege.adventofcode.aoccommon.days.Day
import org.apache.commons.lang3.time.DurationFormatUtils
import org.reflections.Reflections
import java.io.FileOutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * Wrapper class for the AdventOfCode runner. This class will set the necessary parameters to only
 * run specified assignments when wanted.
 *
 * @property output The [java.io.PrintStream] to use. Defaults to [System.out]
 * @property days The instances of the [Day] assignments to execute.Defaults to all 25 [Day]
 * implementations of the year.
 * @constructor Creates an [AdventOfCode] instance by parsing the arguments given to the program.
 * @param parser The [ArgParser] instance to use.
 * @param assignmentLocation The location of the assignment classes.
 */
abstract class AdventOfCode(parser: ArgParser, assignmentLocation: String) {
    val output by parser.storing(
        help = "The file to print output to. If not present, will print to standard out."
    ) {
        PrintStream(FileOutputStream(this, true))
    }.default(System.out!!)

    val days by parser.storing(
        help = "The day assignments to execute. If not present, will execute all 25 Days."
    ) {
        getAssignments<Day>(assignmentLocation, this.split(',').map { it.toInt() })
    }.default(getAssignments<Day>(assignmentLocation, (1..25).toList()))

    /**
     * Returns the String representation of this object.
     * @return The name of this class, split on uppercase letters or numbers.
     */
    override fun toString(): String {
        return this::class.java.simpleName
            .replace("([A-Z0-9]+)".toRegex(), " $1").trim()
    }

    companion object Runner {
        private const val COLUMN_SIZE = 80
        /**
         * Runs the specified assignments
         * @param aoc The [AdventOfCode] instance to use.
         */
        fun run(aoc: AdventOfCode) {
            aoc.run {
                output.println(aoc)
                output.printf(
                    "Started on %s%n",
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().time)
                )
                output.println("Running assignments: ")
                days.joinToString(", ") { it::class.java.simpleName }.let {
                    val tokens = StringTokenizer(it)
                    val result = StringBuilder()

                    var currentLength = 0
                    while (tokens.hasMoreTokens()) {
                        val word = tokens.nextToken()

                        if (currentLength + word.length > COLUMN_SIZE) {
                            result.deleteCharAt(result.length - 1).append("\n")
                            currentLength = 0
                        }
                        result.append(word).append(' ')
                        currentLength += word.length + 1
                    }
                    result.deleteCharAt(result.length - 1).toString()
                }.let(output::println)
//                output.println(days.joinToString(", ") { it::class.java.simpleName })
                val timeTaken = measureTimeMillis {
                    days.onEach { it.run() } // Start all days.
                        .asSequence() // Continue as sequence to print when output present.)
                        .onEach { output.println("-".repeat(COLUMN_SIZE)) }
                        .forEach(output::println) //toString blocks until output present
                }
                output.println("-".repeat(COLUMN_SIZE))
                output.printf(
                    "Total time taken: %ss%n",
                    DurationFormatUtils.formatDurationHMS(timeTaken)
                )
            }
        }

        /**
         * Returns all [Day] instances in a certain location with given identifiers.
         *
         * @param location The package in which to search for [Day] instances.
         * @param assignmentIds The ids of the assignments to select.
         * @return A list of all instances of [Day], or an empty list if none can be found.
         */
        private inline fun <reified E> getAssignments(
            location: String,
            assignmentIds: List<Int>
        ): List<E> {
            val assignments = assignmentIds.map { String.format("Day%02d", it) }
            return Reflections(location)
                .getSubTypesOf(E::class.java)
                .filterNotNull()
                .filter { it.simpleName in assignments }
                .sortedBy { it.simpleName }
                .map { it.newInstance() }
                .toList()
        }
    }
}
