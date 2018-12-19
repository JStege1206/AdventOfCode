package nl.jstege.adventofcode.aoc2018.days.instructionset

import nl.jstege.adventofcode.aoccommon.utils.extensions.copy

/**
 *
 * @author Jelle Stege
 */

data class RegisterBank(private val registers: List<Int>) : List<Int> by registers {
    constructor(vararg r: Int) : this(r.asList())

    fun copyAndUpdate(r: Int, v: Int): RegisterBank = RegisterBank(registers.copy(r to v))
}
