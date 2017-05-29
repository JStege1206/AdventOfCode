package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day04 : Day() {
    private val INPUT_PATTERN = "^([a-z-]+)-(\\d+)\\[([a-z]{5})]$".toRegex()

    private val ROOM_NAME_KEY = 1
    private val SECTOR_ID_KEY = 2
    private val CHECKSUM_KEY = 3

    private val CHECKSUM_LENGTH = 5

    private val SECRET_PHRASE = "northpole object storage"

    override fun first(input: Sequence<String>): Any = input
            .getValidRooms()
            .map {it.sectorId}
            .sum()

    override fun second(input: Sequence<String>): Any = input
            .getValidRooms()
            .find { (encryptedName, sectorId) ->
                SECRET_PHRASE == encryptedName.fold(StringBuilder(), { s, c ->
                    s.append(if (c != '-') 'a' + ((c - 'a' + (sectorId % 26)) % 26) else ' ')
                }).toString().trim()
            }?.sectorId ?: throw IllegalStateException("No answer found")

    private fun Sequence<String>.getValidRooms(): List<Room> {
        return this
                .map {
                    INPUT_PATTERN.matchEntire(it)?.groupValues
                            ?: throw IllegalStateException("Invalid input")
                }
                .map {
                    Room(it[ROOM_NAME_KEY], it[SECTOR_ID_KEY].toInt(), it[CHECKSUM_KEY])
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
                            .fold(StringBuilder(), { s, c -> s.append(c.key) })
                            .toString()
                            .trim()
                }.toList()
    }

    private data class Room(val encryptedName: String, val sectorId: Int, val checksum: String)
}