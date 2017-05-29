package nl.jstege.adventofcode.aoccommon

/**
 * AdventOfCode 2016 implementation
 *
 * @author Jelle Stege
 */
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.ClassUtils
import nl.jstege.adventofcode.aoccommon.utils.extensions.format
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
 */
abstract class AdventOfCode(parser: ArgParser, assignmentLocation: String) {
    val outputStream by parser.storing("-o", "--output",
            help = "The file to print output to. If not present, will print to std out."
    ) { PrintStream(FileOutputStream(this, true)) }.default(System.out!!)
    val days: List<Day>

    init {
        val toRun by parser.storing("-d", "--days",
                help = "The day assignments to execute, split by a single comma. " +
                        "If omitted all days will be executed."
        ) { this.split(',').map { it.toInt() } }.default((1..25).toList())

        days = ClassUtils.getClassesFromPackage<Day>(assignmentLocation)
                .filter { it.simpleName.replace("Day", "").toInt() in toRun }
                .map { it.newInstance() }
    }

    companion object Runner {
        /**
         * Runs the specified assignments
         * @param aoc The [AdventOfCode] instance to use.
         */
        @JvmStatic fun run(aoc: AdventOfCode) {
            aoc.run {
                val startDate = SimpleDateFormat("yy-MM-d HH:mm:ss")
                        .format(Calendar.getInstance().time)
                val toRun = days.map { it::class.java.simpleName }.joinToString(", ")
                outputStream.println("Advent Of Code 2016")
                outputStream.println("Started on $startDate")
                outputStream.println("Running assignments: $toRun")
                val timeTaken = measureTimeMillis {
                    days.onEach { it.run() } // Start all days.
                            .asSequence() // Continue as sequence to print on present output.
                            .map { it.toString() } //toString blocks until output is present.
                            .forEach(outputStream::println)
                }
                outputStream.println("Total time taken: ${Duration.ofMillis(timeTaken).format()}\n")
            }
        }
    }
}