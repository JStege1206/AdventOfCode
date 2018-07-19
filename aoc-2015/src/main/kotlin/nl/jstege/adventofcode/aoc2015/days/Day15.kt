package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.component6
import nl.jstege.adventofcode.aoccommon.utils.extensions.component7
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
        ) = recipe.fold(0) { score, (amt, ing) -> score + amt * ing.let(property) }

        return if (useCalories && calculateScore(recipe, Ingredient::calories) != NEEDED_CALORIES) 0
        else Math.max(calculateScore(recipe, Ingredient::capacity), 0) *
                Math.max(calculateScore(recipe, Ingredient::durability), 0) *
                Math.max(calculateScore(recipe, Ingredient::flavor), 0) *
                Math.max(calculateScore(recipe, Ingredient::texture), 0)
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

            @JvmStatic
            fun parse(input: String): Ingredient {
                return INPUT_REGEX.matchEntire(input)?.groupValues
                    ?.let { (_, name, capacity, durability, flavor, texture, calories) ->
                        Ingredient(
                            name,
                            capacity.toInt(),
                            durability.toInt(),
                            flavor.toInt(),
                            texture.toInt(),
                            calories.toInt()
                        )
                    } ?: throw IllegalArgumentException("Invalid input")
            }
        }
    }
}
