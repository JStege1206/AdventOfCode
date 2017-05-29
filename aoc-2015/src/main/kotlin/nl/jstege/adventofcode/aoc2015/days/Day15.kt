package nl.jstege.adventofcode.aoc2015.days

import nl.jstege.adventofcode.aoccommon.days.Day

/**
 *
 * @author Jelle Stege
 */
class Day15 : Day() {
    private val TEASPOONS = 100
    private val CALORIES = 500

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
        var sprinkle = 0
        while (sprinkle < TEASPOONS) {
            var butterscotch = 0
            while (butterscotch < TEASPOONS - butterscotch) {
                var chocolate = 0
                while (chocolate < TEASPOONS - butterscotch - sprinkle) {
                    val candy = TEASPOONS - chocolate - butterscotch - sprinkle
                    val tScore = this.calculateScore(sprinkle, butterscotch, chocolate, candy, useCalories)

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
        if (useCalories && calories != CALORIES) {
            return 0
        }

        return Math.max(capScore, 0) *
                Math.max(durScore, 0) *
                Math.max(flaScore, 0) *
                Math.max(texScore, 0)
    }

    data class Ingredient(
            val name: String,
            val capacity: Int,
            val durability: Int,
            val flavor: Int,
            val texture: Int,
            val calories: Int) {
        companion object Parser {

            @JvmStatic private val INPUT_REGEX = ("(\\w+): " +
                    "capacity (-?\\d+), " +
                    "durability (-?\\d+), flavor (-?\\d+), " +
                    "texture (-?\\d+), " +
                    "calories (-?\\d+)").toRegex()

            @JvmStatic fun parse(input: String): Ingredient {
                val g = INPUT_REGEX.matchEntire(input)?.groupValues
                        ?: throw IllegalArgumentException("Invalid input")
                return Ingredient(
                        name = g[1],
                        capacity = g[2].toInt(),
                        durability = g[3].toInt(),
                        flavor = g[4].toInt(),
                        texture = g[5].toInt(),
                        calories = g[6].toInt())
            }
        }
    }
}
