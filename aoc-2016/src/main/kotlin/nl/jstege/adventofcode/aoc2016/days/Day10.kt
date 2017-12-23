package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day10 : Day() {
    private companion object Configuration {
        private const val SRC_BOT_INDEX = 1
        private const val DEST1_SELECTOR_INDEX = 5
        private const val DEST1_INDEX = 6
        private const val DEST2_SELECTOR_INDEX = 10
        private const val DEST2_INDEX = 11
        private const val INIT_VAL_INDEX = 1

        private const val INIT_BOT_INDEX = 5
        private const val REQUIRED_LOW_CHIP = 17

        private const val REQUIRED_HIGH_CHIP = 61
        private val OUTPUTS_TO_MULTIPLY = listOf(0, 1, 2)
    }

    override val title: String = "Balance Bots"

    override fun first(input: Sequence<String>): Any {
        val queue = input.toMutableList()
        val bots = mutableMapOf<Int, MutableList<Int>>()
        val outputs = mutableMapOf<Int, Int>()
        while (queue.isNotEmpty()) {
            val line = queue.removeAt(0)
            if (!line.parse(bots, outputs)) {
                queue += line
            }
        }
        return bots.asIterable()
                .find { REQUIRED_LOW_CHIP in it.value && REQUIRED_HIGH_CHIP in it.value }?.key
                ?: throw IllegalStateException("No answer found")
    }

    override fun second(input: Sequence<String>): Any {
        val queue = input.toMutableList()
        val bots = mutableMapOf<Int, MutableList<Int>>()
        val outputs = mutableMapOf<Int, Int>()
        while (queue.isNotEmpty()) {
            val line = queue.removeAt(0)
            if (!line.parse(bots, outputs)) {
                queue += line
            }
        }
        return OUTPUTS_TO_MULTIPLY
                .map { outputs[it] ?: throw IllegalStateException("No answer found") }
                .fold(1, Int::times)
    }

    private fun String.parse(bots: MutableMap<Int, MutableList<Int>>,
                             outputs: MutableMap<Int, Int>): Boolean {
        val elements = this.split(' ')
        if (elements[0] == "value") {
            bots.getOrPut(elements[INIT_BOT_INDEX].toInt(), { mutableListOf() })
                    .add(elements[INIT_VAL_INDEX].toInt())
            return true
        }
        val chips = bots.getOrPut(elements[SRC_BOT_INDEX].toInt(), { mutableListOf() })
        if (chips.size != 2) {
            return false
        }

        moveChip(elements[DEST1_SELECTOR_INDEX], elements[DEST1_INDEX].toInt(),
                chips.min() ?: chips.first(), bots, outputs)
        moveChip(elements[DEST2_SELECTOR_INDEX], elements[DEST2_INDEX].toInt(),
                chips.max() ?: chips.first(), bots, outputs)

        return true
    }

    private fun moveChip(selector: String, dest: Int, chip: Int,
                         bots: MutableMap<Int, MutableList<Int>>,
                         outputs: MutableMap<Int, Int>) {
        if (selector == "bot") {
            bots.getOrPut(dest, { mutableListOf() }).add(chip)
        } else {
            outputs[dest] = chip
        }
    }
}
