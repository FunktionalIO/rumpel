package io.funktional.rumpel

import scala.annotation.tailrec

/**
 * A case class representing a random number generator.
 *
 * @param seed the initial seed value
 */
case class Random(seed: Long):
    private def next: Random = Random(seed * 6364136223846793005L + 1442695040888963407L)

    /**
     * Generates the next random long value.
     *
     * @return a tuple containing the next Random instance and the generated long value
     */
    private def nextLong: (Random, Long) =
        val next = this.next
        (next, seed)

    /**
     * Generates the next random integer value within the specified range.
     *
     * @param n the upper bound (exclusive) for the generated integer value
     * @return a tuple containing the next Random instance and the generated integer value
     */
    def nextInt(n: Int): (Random, Int) =
        val (next, value) = nextLong
        (next, (value >>> 16).toInt % n)

    /**
     * Shuffles the elements of the given list using the random number generator.
     *
     * @param elements the list of elements to shuffle
     * @tparam T the type of elements in the list
     * @return a tuple containing the next Random instance and the shuffled list
     */
    private def shuffle[T](elements: List[T]): (Random, List[T]) =
        @tailrec
        def loop(rng: Random, remaining: List[T], acc: List[T]): (Random, List[T]) =
            if remaining.isEmpty then (rng, acc.reverse)
            else
                val (nextRng, index) = rng.nextInt(remaining.size)
                val (picked, rest)   = remaining.splitAt(index)
                loop(nextRng, picked ++ rest.tail, rest.head :: acc)

        loop(this, elements, Nil)
    end shuffle

    def pick[T](list: List[T], n: Int): (Random, List[T]) =
        val (rng, indices) = shuffle(list.indices.toList)
        (rng, indices.take(n.min(list.size)).sorted.map(list))

end Random
