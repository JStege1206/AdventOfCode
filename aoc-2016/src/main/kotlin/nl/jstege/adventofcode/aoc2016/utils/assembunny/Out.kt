package nl.jstege.adventofcode.aoc2016.utils.assembunny

import nl.jstege.adventofcode.aoccommon.utils.extensions.isCastableToInt
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 * Prints either a literal or a register value to the [Machine]s outStream.
 * @author Jelle Stege
 */
class Out(operands: List<String>, machine: Machine) : Assembunny(operands, machine) {
    override fun invoke() {
        if (operands.isEmpty()) {
            throw IllegalArgumentException("Instruction 'out' was given too few arguments")
        }

        machine.outStream.print(if (operands[0].isCastableToInt()) {
            operands[0].toInt()
        } else {
            machine.registers[operands[0]]
        })

        machine.ir++
    }
}