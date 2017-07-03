package nl.jstege.adventofcode.aoccommon.utils.machine

/**
 * Represents a Program, which is an iterable list of Instructions belonging to a machine.
 * @author Jelle Stege
 */
class Program(var instructions: MutableList<Instruction>,
              val machine: Machine) : Iterable<Instruction> {
    init {
        machine.program = this
    }

    /**
     * The amount of instructions in this program.
     */
    val size = instructions.size


    /**
     * Returns a [ProgramIterator], which iterates over the program. Do note that this iterator may
     * have an infinite amount of elements due to jumping to previous instructions.
     * @return The [ProgramIterator]
     */
    override fun iterator(): Iterator<Instruction> {
        return ProgramIterator()
    }

    /**
     * An [Iterator] used to iterator over a [Program].
     */
    inner class ProgramIterator : Iterator<Instruction> {
        /**
         * Returns the next [Instruction]
         * @return The [Instruction]
         */
        override fun next(): Instruction = instructions[machine.ir]

        /**
         * Checks whether there is a next [Instruction], this is the case when the [Machine]'s ir
         * is larger than or equal to 0 and smaller than the [Program]'s size.
         */
        override fun hasNext(): Boolean = machine.ir in (0..(size - 1))
    }

    companion object Assembler {
//        /**
//         * Assembles a List of Strings to a [Program].
//         * @param rawInstructions A list of [Instruction] representations
//         * @param machine The machine the resulting [Program] will belong to.
//         * @param instructionParser The function that parses the given input to instructions.
//         * @return A [Program] corresponding to the given list of [Instruction] representations
//         */
//        @JvmStatic fun assemble(rawInstructions: List<String>, machine: Machine,
//                                instructionParser: (List<String>) -> List<Instruction>) =
//            Program(instructionParser(rawInstructions).map {
//                it.machine = machine
//                it
//            }.toMutableList(), machine)


        /**
         * Assembles a List of Strings to a [Program]. Will also optimize the resulting program to
         * run more efficiently.
         * @param rawInstructions A list of [Instruction] representations
         * @param machine The machine the resulting [Program] will belong to.
         * @param instructionParser The function that parses the given input to instructions.
         * @param optimizer The optimizer to use.
         * @return A [Program] corresponding to the given list of [Instruction] representations
         */
        @JvmStatic fun assemble(rawInstructions: List<String>, machine: Machine,
                                instructionParser: (List<String>) -> List<Instruction>,
                                optimizer: (MutableList<Instruction>) ->
                               MutableList<Instruction> = { it }) =
                Program(optimizer(instructionParser(rawInstructions)
                        .onEach { it.machine = machine }
                        .toMutableList()
                ), machine)

    }
}