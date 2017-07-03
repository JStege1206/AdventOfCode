package nl.jstege.adventofcode.aoccommon.utils.machine

/**
 * Represents a virtual machine. Does not contain memory, only registers.
 * @author Jelle Stege
 */
class Machine {
    /**
     * The key used to store the instruction pointer.
     */
    val IR_KEY = "ir"
    /**
     * The registers for this machine.
     */
    val registers = Registers()
    /**
     * The program to run on this machine.
     */
    lateinit var program: Program
    /**
     * The [java.io.PrintStream] to use for output. All output of the given program will be printed
     * to this stream.
     */
    var outStream = System.out!!
    /**
     * The [java.io.InputStream] to use for input. All input needed by the program will come from
     * this stream.
     */
    var inStream = System.`in`!!
    /**
     * The instruction pointer used by the machine. Will retrieve the actual IR from the registers.
     */
    var ir: Int
        get() = registers[IR_KEY]
        set(value) {
            registers[IR_KEY] = value
        }

    /**
     * Initiates a machine with the IR set to 0
     */
    init {
        registers[IR_KEY] = 0
    }

    /**
     * Resets the machine by clearing registers and the instruction pointer.
     */
    fun reset() {
        registers.clear()
        registers[IR_KEY] = 0
    }
}