package nl.jstege.adventofcode.aoccommon.utils.machine

/**
 * Represents an Instruction. A [Program] consists of multiple of these instructions.
 * @author Jelle Stege
 */
interface Instruction {
    /**
     * Executes the instruction.
     */
    operator fun invoke()

    /**
     * The operands of this [Instruction] as a list of Strings. The [Instruction]'s invoke method
     * should check whether to assume the operands are literals, register indices or memory
     * addresses.
     */
    var operands: List<String>

    /**
     * The machine this Instruction belongs to. The machine is used to handle anything outside of
     * the operands reach, like modifying the ir or printing to the out stream.
     */
    var machine: Machine

    /**
     * Returns this Instruction.
     * @return This object.
     */
    operator fun component1() = this

    /**
     * Returns the operands of the Instruction.
     * @return The operands for this instruction.
     */
    operator fun component2() = operands

    /**
     * Returns the machine of this Instruction.
     * @return The machine used for this instruction.
     */
    operator fun component3() = machine
}