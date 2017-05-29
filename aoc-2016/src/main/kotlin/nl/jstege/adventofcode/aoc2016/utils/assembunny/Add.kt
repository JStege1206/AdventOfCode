package nl.jstege.adventofcode.aoc2016.utils.assembunny

import nl.jstege.adventofcode.aoccommon.utils.machine.Machine

/**
 * An Add instruction. Adds 2 registers and stores it in the first register.
 * Also sets the second register to 0. Jumps 3 instructions forward afterwards
 * @author Jelle Stege
 */
class Add(operands: List<String>, machine: Machine) : Assembunny(operands, machine) {
    override fun invoke() {
        if (operands.size < 2) {
            throw IllegalArgumentException("Instruction 'add' was given too few arguments")
        }
        machine.registers[operands[0]] += machine.registers[operands[1]]
        machine.registers[operands[1]] = 0
        machine.ir += 3
    }
}
