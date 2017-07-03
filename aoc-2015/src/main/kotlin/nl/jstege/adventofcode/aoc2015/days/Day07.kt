package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.component6

/**
 *
 * @author Jelle Stege
 */
class Day07 : Day() {
    private val INPUT_REGEX =
            "^(NOT)?\\s?([a-z0-9]+)\\s?(AND|OR|LSHIFT|RSHIFT)?\\s?([a-z0-9]*) -> ([a-z]+)$".toRegex()

    override fun first(input: Sequence<String>) = input.compute()

    override fun second(input: Sequence<String>) = input.compute("b" to Wire(3176))

    private fun Sequence<String>.compute(vararg init: Pair<String, Wire>) = this
            .map {
                INPUT_REGEX.matchEntire(it)?.groupValues
                        ?: throw IllegalArgumentException("Invalid input")
            }
            .fold(mutableMapOf(*init), { wires, (_, opNot, w1, opR, w2, wo) ->
                val op = if (opNot.isNotEmpty()) {
                    Operator.NOT
                } else if (opR.isNotEmpty()) {
                    Operator.valueOf(opR)
                } else {
                    Operator.MOVE
                }

                val wireOut = Wire.get(wo, wires)
                wireOut.gate = Gate(op,
                        Wire.get(w1, wires), Wire.get(w2, wires),
                        wireOut)
                wires
            })
            .getOrElse("a", { Wire() })
            .value

    data class Gate(val op: Operator, val wire1: Wire, val wire2: Wire, val wireOut: Wire) {
        operator fun invoke() {
            wireOut.value = when (op) {
                Operator.MOVE -> wire1.value
                Operator.AND -> wire1.value and wire2.value
                Operator.OR -> wire1.value or wire2.value
                Operator.LSHIFT -> wire1.value shl wire2.value
                Operator.RSHIFT -> wire1.value ushr wire2.value
                Operator.NOT -> (wire1.value.inv()) and 0xFFFF
            }
        }
    }

    data class Wire(private val _value: Int = -1) {
        var value = _value
            get() {
                if (field < 0) {
                    gate()
                }
                return field
            }
        lateinit var gate: Gate

        companion object {
            @JvmStatic fun get(ident: String, wires: MutableMap<String, Wire>) =
                    if (ident.matches("[0-9]+".toRegex())) {
                        Wire(ident.toInt())
                    } else {
                        wires.getOrPut(ident, { Wire() })
                    }
        }
    }


    enum class Operator {
        AND, OR, LSHIFT, RSHIFT, NOT, MOVE
    }
}