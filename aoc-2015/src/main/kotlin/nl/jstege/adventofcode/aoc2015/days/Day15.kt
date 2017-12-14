package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day
import nl.jstege.adventofcode.aoccommon.utils.extensions.component6
import nl.jstege.adventofcode.aoccommon.utils.extensions.component7

/**
 *
 * @author Jelle Stege
 */
class Day15 : Day() {
    private companion object Configuration {
        private val TEA_SPOONS_AMOUNT = 100
        private val NEEDED_CALORIES = 500
    }

    override suspend fun first(input: Sequence<String>) = input
            .map(Ingredient.Parser::parse)
            .associate { it.name to it }
            .calculateMaxScore()


    override suspend fun second(input: Sequence<String>) = input
            .map(Ingredient.Parser::parse)
            .associate { it.name to it }
            .calculateMaxScore(true)

    private fun Map<String, Ingredient>.calculateMaxScore(useCalories: Boolean = false): Int {
        var score = 0
        var sprinkle = 0
        while (sprinkle < TEA_SPOONS_AMOUNT) {
            var butterscotch = 0
            while (butterscotch < TEA_SPOONS_AMOUNT - butterscotch) {
                var chocolate = 0
                while (chocolate < TEA_SPOONS_AMOUNT - butterscotch - sprinkle) {
                    val candy = TEA_SPOONS_AMOUNT - chocolate - butterscotch - sprinkle
                    val tScore = this.calculateScore(sprinkle, butterscotch, chocolate, candy,
                            useCalories)

                    if (tScore > score) {
                        score = tScore
                    }
                    chocolate++
                }
                butterscotch++
            }
            sprinkle++
        }
        return score
    }

    private fun Map<String, Ingredient>.calculateScore(
            sprinkle: Int, butterscotch: Int, chocolate: Int, candy: Int,
            useCalories: Boolean): Int {
        val sprinkles = this["Sprinkles"]!!
        val butterscotches = this["Butterscotch"]!!
        val chocolates = this["Chocolate"]!!
        val candies = this["Candy"]!!

        val capScore = sprinkle * sprinkles.capacity + butterscotch * butterscotches.capacity +
                chocolate * chocolates.capacity + candy * candies.capacity
        val durScore = sprinkle * sprinkles.durability + butterscotch * butterscotches.durability +
                chocolate * chocolates.durability + candy * candies.durability
        val flaScore = sprinkle * sprinkles.flavor + butterscotch * butterscotches.flavor +
                chocolate * chocolates.flavor + candy * candies.flavor
        val texScore = sprinkle * sprinkles.texture + butterscotch * butterscotches.texture +
                chocolate * chocolates.texture + candy * candies.texture
        val calories = sprinkle * sprinkles.calories + butterscotch * butterscotches.calories +
                chocolate * chocolates.calories + candy * candies.calories
        if (useCalories && calories != NEEDED_CALORIES) {
            return 0
        }

        return Math.max(capScore, 0) *
                Math.max(durScore, 0) *
                Math.max(flaScore, 0) *
                Math.max(texScore, 0)
    }

    private data class Ingredient(val name: String, val capacity: Int, val durability: Int,
                                  val flavor: Int, val texture: Int, val calories: Int) {
        companion object Parser {
            private val INPUT_REGEX = ("(\\w+): " +
                    "capacity (-?\\d+), " +
                    "durability (-?\\d+), flavor (-?\\d+), " +
                    "texture (-?\\d+), " +
                    "calories (-?\\d+)").toRegex()

            fun parse(input: String): Ingredient {
                val (_, name, capacity, durability, flavor, texture, calories) = INPUT_REGEX
                        .matchEntire(input)?.groupValues
                        ?: throw IllegalArgumentException("Invalid input")
                return Ingredient(name, capacity.toInt(), durability.toInt(),
                        flavor.toInt(), texture.toInt(), calories.toInt())
            }
        }
    }
}
