package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoc2016.utils.assembunny.Assembunny
import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.isEven
import nl.jstege.adventofcode.aoccommon.utils.extensions.isOdd
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine
import nl.jstege.adventofcode.aoccommon.utils.machine.Program
import nl.jstege.adventofcode.aoccommon.utils.machine.Simulator
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 *
 * @author Jelle Stege
 */
class Day25 : Day() {
    private companion object Configuration {
        private const val TEST_STRING_LENGTH = 16
        private const val INPUT_REGISTER = "a"
        private const val STEP_SIZE = 1000
    }

    override suspend fun first(input: Sequence<String>): Any {
        val os = ByteArrayOutputStream()
        val ps = PrintStream(os)
        val machine = Machine()
        val program = Program.assemble(input.toList(), machine,
                { Assembunny.assemble(it, machine) },
                { Assembunny.optimize(it) })
        val sim = Simulator(program, machine)
        machine.outStream = ps

        return (0 until Int.MAX_VALUE).asSequence()
                .onEach {
                    os.reset()
                    machine.reset()
                    machine.registers[INPUT_REGISTER] = it
                }
                .first {
                    var isAlternating: Boolean
                    do {
                        sim.step(STEP_SIZE)
                        isAlternating = os.toByteArray().isAlternating()
                    } while (isAlternating && os.size() < TEST_STRING_LENGTH)
                    isAlternating
                }

    }

    override suspend fun second(input: Sequence<String>): Any = "-"

    private fun ByteArray.isAlternating(): Boolean = this.size < 2 || this
            .withIndex()
            .partition { (i, _) -> i.isEven() }
            .let { (es, os) ->
                es.first().value != os.first().value
                        && es.map { it.value }.toSet().size == 1
                        && os.map { it.value }.toSet().size == 1
            }
}
