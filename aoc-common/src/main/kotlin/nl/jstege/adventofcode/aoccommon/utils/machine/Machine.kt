package nl.jstege.adventofcode.aoccommon.utils.machine

/**
 * Represents a virtual machine. Does not contain memory, only registers.
 * @author Jelle Stege
 */
class Machine {
    val IR_KEY = "ir"
    val registers = Registers()
    lateinit var program: Program
    var outStream = System.out!!
    var inStream = System.`in`!!
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

    fun reset() {
        registers.clear()
        registers[IR_KEY] = 0
    }
}