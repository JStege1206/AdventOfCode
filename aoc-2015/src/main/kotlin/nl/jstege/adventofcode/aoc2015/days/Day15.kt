package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.component6
import nl.jstege.adventofcode.aoccommon.utils.extensions.component7
import nl.jstege.adventofcode.aoccommon.utils.extensions.extractValues
import kotlin.math.max
import kotlin.reflect.KProperty1

/**
 *
 * @author Jelle Stege
 */
class Day15 : Day(title = "Science for Hungry People") {
    private companion object Configuration {
        private const val TEA_SPOONS_AMOUNT = 100
        private const val NEEDED_CALORIES = 500
        private const val SPRINKLES = "Sprinkles"
        private const val BUTTERSCOTCH = "Butterscotch"
        private const val CHOCOLATE = "Chocolate"
        private const val CANDY = "Candy"
    }

    override fun first(input: Sequence<String>) = input
        .map(Ingredient.Parser::parse)
        .associate { it.name to it }
        .calculateMaxScore()


    override fun second(input: Sequence<String>) = input
        .map(Ingredient.Parser::parse)
        .associate { it.name to it }
        .calculateMaxScore(true)

    private fun Map<String, Ingredient>.calculateMaxScore(useCalories: Boolean = false): Int {
        var score = 0
        (0 until TEA_SPOONS_AMOUNT).forEach { sprinkle ->
            (0 until TEA_SPOONS_AMOUNT - sprinkle).forEach { butterscotch ->
                (0 until TEA_SPOONS_AMOUNT - butterscotch - sprinkle).forEach { chocolate ->
                    val candy = TEA_SPOONS_AMOUNT - chocolate - butterscotch - sprinkle
                    score = max(
                        score,
                        calculateScore(
                            sprinkle to this[SPRINKLES]!!,
                            butterscotch to this[BUTTERSCOTCH]!!,
                            chocolate to this[CHOCOLATE]!!,
                            candy to this[CANDY]!!,
                            useCalories = useCalories
                        )
                    )
                }
            }
        }
        return score
    }

    private fun calculateScore(vararg recipe: Pair<Int, Ingredient>, useCalories: Boolean): Int {
        fun calculateScore(
            recipe: Array<out Pair<Int, Ingredient>>,
            property: KProperty1<Ingredient, Int>
        ) = recipe.sumBy { (amount, ingredient) -> amount * ingredient.run(property) }

        return if (useCalories && calculateScore(recipe, Ingredient::calories) != NEEDED_CALORIES) 0
        else setOf(
            Ingredient::capacity,
            Ingredient::durability,
            Ingredient::flavor,
            Ingredient::texture
        ).let {
            it.fold(1) { acc, property -> acc * Math.max(calculateScore(recipe, property), 0) }
        }
    }

    private data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
    ) {
        companion object Parser {
            private val INPUT_REGEX = ("(\\w+): " +
                    "capacity (-?\\d+), " +
                    "durability (-?\\d+), flavor (-?\\d+), " +
                    "texture (-?\\d+), " +
                    "calories (-?\\d+)").toRegex()

            private const val NAME_INDEX = 1
            private const val CAPACITY_INDEX = 2
            private const val DURABILITY_INDEX = 3
            private const val FLAVOR_INDEX = 4
            private const val TEXTURE_INDEX = 5
            private const val CALORIES_INDEX = 6

            private val PARAM_INDICES = intArrayOf(
                NAME_INDEX,
                CAPACITY_INDEX,
                DURABILITY_INDEX,
                FLAVOR_INDEX,
                TEXTURE_INDEX,
                CALORIES_INDEX
            )

            @JvmStatic
            fun parse(input: String): Ingredient {
                return input.extractValues(INPUT_REGEX, *PARAM_INDICES)
                    .let { (name, capacity, durability, flavor, texture, calories) ->
                        Ingredient(
                            name,
                            capacity.toInt(),
                            durability.toInt(),
                            flavor.toInt(),
                            texture.toInt(),
                            calories.toInt()
                        )
                    }
            }
        }
    }
}
