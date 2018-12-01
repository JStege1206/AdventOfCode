package nl.jstege.adventofcode.aoc2016.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValue
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues

/**
 *
 * @author Jelle Stege
 */
class Day04 : Day(title = "Security Through Obscurity") {
    private companion object Configuration {
        private const val CHECKSUM_LENGTH = 5
        private const val SECRET_PHRASE = "northpole object storage"
        private const val INPUT_PATTERN_STRING = """^([a-z-]+)-(\d+)\[([a-z]{5})]$"""
        private val INPUT_REGEX = INPUT_PATTERN_STRING.toRegex()

        private const val NAME_INDEX = 1
        private const val ID_INDEX = 2
        private const val CHECKSUM_INDEX = 3

        private val PARAM_INDICES = intArrayOf(NAME_INDEX, ID_INDEX, CHECKSUM_INDEX)
    }

    override fun first(input: Sequence<String>): Any = input
        .getValidRooms()
        .map { it.sectorId }
        .sum()

    override fun second(input: Sequence<String>): Any = input
        .getValidRooms()
        .find { (encryptedName, sectorId, _) ->
            SECRET_PHRASE == encryptedName.asSequence()
                .map { if (it != '-') 'a' + ((it - 'a' + (sectorId % 26)) % 26) else ' ' }
                .joinToString("")
        }?.sectorId ?: throw IllegalStateException("No answer found")

    private fun Sequence<String>.getValidRooms(): Sequence<Room> {
        return this
            .map { it.extractValues(INPUT_REGEX, *PARAM_INDICES) }
            .map { (name, id, checksum) -> Room(name, id.toInt(), checksum) }
            .filter { room ->
                room.checksum == room.encryptedName
                    .filter { it != '-' }
                    .groupBy { it }
                    .asIterable()
                    .sortedWith(Comparator { (k, v), (ok, ov) ->
                        if (v.size > ov.size || (v.size == ov.size && k < ok)) -1 else 1
                    })
                    .take(CHECKSUM_LENGTH)
                    .map { it.key }
                    .joinToString("")
            }
    }

    private data class Room(val encryptedName: String, val sectorId: Int, val checksum: String)
}
