package nl.jstege.adventofcode.aoc2015.utils.instructionset

import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 *
 * @author Jelle Stege
 */
class Hlf(operands: List<String>, machine: Machine) : SimpleInstructionSet(operands, machine) {
    override fun invoke() {
        if (operands.isEmpty()) {
            throw IllegalArgumentException("Instruction 'hlf' was given too few arguments")
        }
        machine.registers[operands[0]] /= 2
        machine.ir++
    }
}