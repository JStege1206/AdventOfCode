package nl.jstege.adventofcode.aoccommon.utils.machine

/**
 * Represent a registerbank.
 * @author Jelle Stege
 */
class Registers {
    private val registers = mutableMapOf<String, Int>()

    /**
     * Returns the value of a register, or 0 if not present.
     * @return The value of this register, or 0 if not present.
     */
    operator fun get(key: String): Int {
        return registers.getOrPut(key, { 0 })
    }

    /**
     * Sets a register to a certain value.
     * @param key The register name.
     * @param value The register value.
     */
    operator fun set(key: String, value: Int) {
        registers[key] = value
    }

    /**
     * Clears all values in the register bank.
     */
    fun clear() {
        registers.clear()
    }

    /**
     * Sets all entries of the given map in this register.
     * @param from the entries to set.
     */
    fun putAll(from: Map<String, Int>) {
        registers.putAll(from)
    }
}