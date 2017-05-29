package nl.jstege.adventofcode.aoc2016.utils.assembunny

import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 * A decrement instruction. Decrements the given register by 1.
 * @author Jelle Stege
 */
class Dec(operands: List<String>, machine: Machine) : Assembunny(operands, machine) {
    override fun invoke() {
        if (operands.isEmpty()) {
            throw IllegalArgumentException("Instruction 'dec' was given too few arguments")
        }
        machine.registers[operands[0]]--
        machine.ir++
    }
}