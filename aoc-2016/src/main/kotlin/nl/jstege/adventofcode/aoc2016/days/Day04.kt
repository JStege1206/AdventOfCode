package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day04 : Day() {
    private val INPUT_PATTERN = "^([a-z-]+)-(\\d+)\\[([a-z]{5})]$".toRegex()

    private val CHECKSUM_LENGTH = 5

    private val SECRET_PHRASE = "northpole object storage"

    override fun first(input: Sequence<String>): Any = input
            .getValidRooms()
            .map { it.sectorId }
            .sum()

    override fun second(input: Sequence<String>): Any = input
            .getValidRooms()
            .find { (encryptedName, sectorId, _) ->
                SECRET_PHRASE == encryptedName.asSequence()
                        .map {
                            if (it != '-') 'a' + ((it - 'a' + (sectorId % 26)) % 26)
                            else ' '
                        }
                        .fold(StringBuilder(), StringBuilder::append)
                        .trim()
                        .toString()
            }?.sectorId ?: throw IllegalStateException("No answer found")

    private fun Sequence<String>.getValidRooms(): Sequence<Room> {
        return this
                .map {
                    INPUT_PATTERN.matchEntire(it)?.groupValues
                            ?: throw IllegalStateException("Invalid input")
                }
                .map { (_, name, id, checksum) ->
                    Room(name, id.toInt(), checksum)
                }
                .filter {
                    it.checksum == it.encryptedName
                            .filter { it != '-' }
                            .groupBy { it }
                            .asIterable()
                            .sortedWith(Comparator { (k, v), (ok, ov) ->
                                if (v.size > ov.size || (v.size == ov.size && k < ok)) -1
                                else 1
                            })
                            .take(CHECKSUM_LENGTH)
                            .map { it.key }
                            .fold(StringBuilder(), StringBuilder::append)
                            .trim()
                            .toString()

                }
    }

    private data class Room(val encryptedName: String, val sectorId: Int, val checksum: String)
}