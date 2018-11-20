package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day21 : Day(title = "RPG Simulator 20XX") {
    private companion object Configuration {
        private val ITEMS = listOf(
            8 to Item.Weapon(4),
            10 to Item.Weapon(5),
            24 to Item.Weapon(6),
            40 to Item.Weapon(7),
            74 to Item.Weapon(8),
            0 to Item.Armor(0),
            13 to Item.Armor(1),
            31 to Item.Armor(2),
            53 to Item.Armor(3),
            75 to Item.Armor(4),
            102 to Item.Armor(5),
            0 to Item.Ring(0, Item.ItemType.ATTACK),
            0 to Item.Ring(0, Item.ItemType.ATTACK),
            25 to Item.Ring(1, Item.ItemType.ATTACK),
            50 to Item.Ring(2, Item.ItemType.ATTACK),
            100 to Item.Ring(3, Item.ItemType.ATTACK),
            20 to Item.Ring(1, Item.ItemType.DEFENSE),
            40 to Item.Ring(2, Item.ItemType.DEFENSE),
            80 to Item.Ring(3, Item.ItemType.DEFENSE)
        )

        private val WEAPONS = ITEMS.filter { it.second is Item.Weapon }
        private val ARMOR = ITEMS.filter { it.second is Item.Armor }
        private val RINGS = ITEMS.filter { it.second is Item.Ring }

        private val OWN_PLAYER = listOf(
            "Hit Points: 100",
            "Damage: 0",
            "Armor: 0"
        )
    }

    override fun first(input: Sequence<String>) =
        generateBuilds(Player.of(OWN_PLAYER))
            .generateFights(Player.of(input.toList()))
            .filter { it.second }.map { it.first }.min()!!

    override fun second(input: Sequence<String>) =
        generateBuilds(Player.of(OWN_PLAYER))
            .generateFights(Player.of(input.toList()))
            .filter { !it.second }.map { it.first }.max()!!

    private fun generateBuilds(player: Player) =
        WEAPONS.flatMap { weapon ->
            ARMOR.flatMap { armor ->
                RINGS.flatMap { ring1 ->
                    RINGS.filter { ring -> ring !== ring1 }
                        .map { ring2 -> listOf(weapon, armor, ring1, ring2) }
                }
            }
        }
            .asSequence()
            .map { build ->
                val p = player.copy()
                p.weapon = build[0].second as Item.Weapon
                p.armor = build[1].second as Item.Armor
                p.rings[0] = build[2].second as Item.Ring
                p.rings[1] = build[3].second as Item.Ring
                build.sumBy { it.first } to p
            }


    private fun Sequence<Pair<Int, Player>>.generateFights(boss: Player) =
        this.map { (cost, player) -> cost to player.fight(boss.copy()) }


    private data class Player(var hitPoints: Int, var unarmedAttack: Int, var unarmedDefense: Int) {
        var weapon = Item.Weapon(unarmedAttack)
        var armor = Item.Armor(unarmedDefense)
        val rings = mutableListOf(
            Item.Ring(0, Item.ItemType.ATTACK),
            Item.Ring(0, Item.ItemType.ATTACK)
        )
        val attack: Int
            get() = weapon.rating + rings
                .filter { it.itemType == Item.ItemType.ATTACK }
                .sumBy { it.rating }
        val defense: Int
            get() = armor.rating + rings
                .filter { it.itemType == Item.ItemType.DEFENSE }
                .sumBy { it.rating }

        fun fight(other: Player) =
            this.hitPoints - (other.attack - this.defense) *
                    (Math.ceil(other.hitPoints / (this.attack - other.defense).toDouble())
                        .toInt() - 1) >= 0

        companion object Parser {
            @JvmStatic
            fun of(input: List<String>): Player {
                val hp = input[0].substring(12).toInt()
                val atk = input[1].substring(8).toInt()
                val def = input[2].substring(7).toInt()
                return Player(hp, atk, def)
            }
        }
    }

    private sealed class Item(val rating: Int, val itemType: ItemType) {
        class Weapon(rating: Int) : Item(rating, ItemType.ATTACK)
        class Armor(rating: Int) : Item(rating, ItemType.DEFENSE)
        class Ring(rating: Int, itemType: ItemType) : Item(rating, itemType)

        enum class ItemType {
            ATTACK, DEFENSE
        }
    }
}
