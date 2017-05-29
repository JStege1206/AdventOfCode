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
    private val TEST_STRING_LENGTH = 16

    private val MAX_CHECK = Int.MAX_VALUE
    private val INPUT_REGISTER = "a"
    private val STEP_SIZE = 1000

    override fun first(input: Sequence<String>): Any {
        val os = ByteArrayOutputStream()
        val ps = PrintStream(os)
        val machine = Machine()
        val program = Program.assemble(input.toList(), machine,
                { Assembunny.assemble(it, machine) },
                { Assembunny.optimize(it) })
        val sim = Simulator(program, machine)
        machine.outStream = ps

        var initValue = 0
        var isAlternating: Boolean
        while (initValue < MAX_CHECK) {
            os.reset()
            machine.reset()
            machine.registers[INPUT_REGISTER] = initValue

            do {
                sim.step(STEP_SIZE)
                isAlternating = os.toByteArray().isAlternating()
            } while (isAlternating && os.size() < TEST_STRING_LENGTH)

            if (isAlternating) {
                break
            }
            initValue++
        }

        return initValue
    }

    override fun second(input: Sequence<String>): Any {
        return "-"
    }

    private fun ByteArray.isAlternating(): Boolean {
        if (this.size <= 2) {
            if (this.size == 2 && this[0] == this[1]) {
                return false
            }
            return true
        }
        val (c1, c2) = this
        this.forEachIndexed { i, b ->
            if (i.isEven() && b != c1 || i.isOdd() && b != c2) {
                return false
            }
        }
        return true
    }
}