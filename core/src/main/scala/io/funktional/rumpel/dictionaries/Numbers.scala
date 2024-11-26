package io.funktional.rumpel.dictionaries

import io.funktional.rumpel.Dictionary
import io.funktional.rumpel.Random

/**
 * A dictionary of numbers.
 *
 * @param maxValue the maximum value for the numbers in the dictionary
 */
case class Numbers(maxValue: Int) extends Dictionary:
    /**
     * Picks a random number from the dictionary.
     *
     * @param random an instance of Random used to generate random numbers
     * @return a tuple containing the updated Random instance and the picked number as a string
     */
    override def pickOne(random: Random): (Random, String) =
        val (rng, index) = random.nextInt(maxValue)
        (rng, index.toString)
end Numbers
