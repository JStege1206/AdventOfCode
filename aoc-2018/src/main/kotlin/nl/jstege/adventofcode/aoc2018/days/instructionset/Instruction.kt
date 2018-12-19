package nl.jstege.adventofcode.aoc2018.days.instructionset

/**
 *
 * @author Jelle Stege
 */
sealed class Instruction {
    data class OpCodeInstruction(val opCode: Int, val op1: Int, val op2: Int, val op3: Int) :
        Instruction()

    data class OpInstruction(val op: Op, val op1: Int, val op2: Int, val op3: Int) :
        Instruction()
}
