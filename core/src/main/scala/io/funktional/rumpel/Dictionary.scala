// Copyright (c) 2024-2024 by Raphaël Lemaitre and Contributors
// This software is licensed under the Eclipse Public License v2.0 (EPL-2.0).
// For more information see LICENSE or https://opensource.org/license/epl-2-0

package io.funktional.rumpel

/**
 * A trait representing a dictionary that can pick a random word.
 */
trait Dictionary:
    /**
     * Picks a random word from the dictionary.
     *
     * @param random an instance of Random used to generate random numbers
     * @return a tuple containing the updated Random instance and the picked word
     */
    def pickOne(random: Random): (Random, String)
end Dictionary

/**
 * Companion object for the Dictionary trait.
 */
object Dictionary:
    /**
     * A trait representing a dictionary based on a list of words.
     */
    trait ListBased extends Dictionary:
        /**
         * A list of words in the dictionary.
         */
        protected def words: List[String]

        /**
         * Picks a random word from the list of words.
         *
         * @param random an instance of Random used to generate random numbers
         * @return a tuple containing the updated Random instance and the picked word
         */
        def pickOne(random: Random): (Random, String) =
            val (rng, index) = random.nextInt(words.size)
            (rng, words(index))
    end ListBased
end Dictionary
