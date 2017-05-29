package nl.jstege.adventofcode.aoc2015.utils.instructionset

import nl.jstege.adventofcode.aoccommon.utils.extensions.isCastableToInt
import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 *
 * @author Jelle Stege
 */
class Jmp(operands: List<String>, machine: Machine) : SimpleInstructionSet(operands, machine) {
    override fun invoke() {
        if (operands.isEmpty()) {
            throw IllegalArgumentException("Instruction 'jmp' was given too few arguments")
        }

        val jmp = if (operands[0].isCastableToInt()) {
            operands[0].toInt()
        } else {
            machine.registers[operands[0]]
        }
        machine.ir += jmp
    }
}