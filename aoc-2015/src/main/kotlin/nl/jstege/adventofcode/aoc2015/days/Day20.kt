package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.head


/**
 *
 * @author Jelle Stege
 */
class Day20 : Day() {
    private val FIRST_PRESENTS_PER_HOUSE = 10
    private val SECOND_PRESENTS_PER_HOUSE = 11
    private val MAX_HOUSES_VISITED = 50

    override fun first(input: Sequence<String>): Any {
        val wantedPresents = input.head.toInt()
        val elves = mutableMapOf<Int, MutableList<Int>>()
        var elf = 1
        var presents = 0
        while (elf <= wantedPresents / 10 && presents <= wantedPresents) {
            val elfList = elves.getOrPut(elf, { mutableListOf() })
            elfList += elf
            presents = elfList.sum() * FIRST_PRESENTS_PER_HOUSE

            if (presents >= wantedPresents) {
                break
            }

            elfList.filter { elf + it <= wantedPresents / 10 }
                    .forEach { elves.getOrPut(elf + it, { mutableListOf() }) += it }
            elf++
        }
        return elf
    }

    override fun second(input: Sequence<String>): Any {
        val wantedPresents = input.head.toInt()
        val elves = mutableMapOf<Int, MutableList<Int>>()
        var elf = 1
        var presents = 0
        while (elf <= (wantedPresents / 10) && presents <= wantedPresents) {
            if (elf !in elves) {
                elves.put(elf, mutableListOf())
            }
            val elfList = elves.remove(elf)!!
            elfList += elf

            presents = elfList.sum() * SECOND_PRESENTS_PER_HOUSE

            if (presents >= wantedPresents) {
                break
            }

            elfList.filter { elf + it < wantedPresents / 10 && elf + it <= it * MAX_HOUSES_VISITED }
                    .forEach { elves.getOrPut(elf + it, { mutableListOf() }) += it }
            elf++
        }
        return elf
    }
}