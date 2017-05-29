package nl.jstege.adventofcode.aoccommon.utils.machine

/**
 * Simulates the execution of a [Program] on a [Machine]
 * @author Jelle Stege
 */
class Simulator(val program: Program, val machine: Machine) {

    /**
     * Will run all [Instruction]s in the [Program]'s iterator. Note that this may cause an infinite
     * loop when the [Program] does not have an ending.
     */
    fun run() {
        program.forEach {
            it()
        }
    }

    /**
     * Executes a single [Instruction] of the [Program]
     */
    fun step() {
        val it = program.iterator()
        if (it.hasNext()) {
            (it.next())()
        }
    }

    /**
     * Executes a certain amount of [Instruction]s of the [Program], or the most present in the
     * [Program]'s iterator.
     * @param steps The amount of [Instruction]s to execute.
     */
    fun step(steps: Int) {
        var s = steps
        val it = program.iterator()
        while (s > 0 && it.hasNext()) {
            (it.next())()
            s--
        }
    }
}