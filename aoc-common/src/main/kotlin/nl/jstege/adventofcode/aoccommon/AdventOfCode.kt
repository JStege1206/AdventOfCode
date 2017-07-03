package nl.jstege.adventofcode.aoccommon

/**
 * AdventOfCode 2016 implementation
 *
 * @author Jelle Stege
 */
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.format
import org.reflections.Reflections
import java.io.FileOutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * Wrapper class for the AdventOfCode runner. This class will set the necessary parameters to only
 * run specified assignments when wanted.
 *
 * @param parser The [ArgParser] instance to use.
 * @param assignmentLocation The location of the assignment classes.
 * @property output The [java.io.PrintStream] to use. Defaults to [System.out]
 * @property days The instances of the [Day] assignments to execute.Defaults to all 25 [Day]
 * implementations of the year.
 * @constructor Creates an [AdventOfCode] instance by parsing the arguments given to the program.
 */
abstract class AdventOfCode(parser: ArgParser, assignmentLocation: String) {
    val output by parser.storing(
            help = "The file to print output to. If not present, will print to std out."
    ) { PrintStream(FileOutputStream(this, true)) }.default(System.out!!)

    val days by parser.storing(help = "The day assignments to execute. " +
            "If not present, will execute all 25 Days.") {
        getAssignments<Day>(assignmentLocation,
                *(this.split(',').map { it.toInt() }.toIntArray()))
    }.default(getAssignments<Day>(assignmentLocation, *(1..25).toList().toIntArray()))

    /**
     * Returns the String representation of this object.
     * @return The name of this class, split on uppercase letters or numbers.
     */
    override fun toString(): String {
        return this::class.java.simpleName
                .replace("([A-Z0-9]+)".toRegex(), " $1").trim()
    }

    companion object Runner {
        /**
         * Runs the specified assignments
         * @param aoc The [AdventOfCode] instance to use.
         */
        fun run(aoc: AdventOfCode) {
            aoc.run {
                output.println(aoc)
                output.print("Started on ")
                output.println(SimpleDateFormat("yy-MM-d HH:mm:ss")
                        .format(Calendar.getInstance().time))
                output.print("Running assignments: ")
                output.println(days.map { it::class.java.simpleName }.joinToString(", "))
                val timeTaken = measureTimeMillis {
                    days.onEach { it.run() } // Start all days.
                            .asSequence() // Continue as sequence to print when output present.
                            .forEach(output::println) //toString blocks until output present
                }
                output.println("Total time taken: " + Duration.ofMillis(timeTaken).format())
            }
        }

        private inline fun <reified E> getAssignments(location: String, vararg ass: Int): List<E> {
            val assignments = ass.map { String.format("Day%02d", it) }
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