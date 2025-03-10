// Copyright (c) 2024-2024 by Raphaël Lemaitre and Contributors
// This software is licensed under the Eclipse Public License v2.0 (EPL-2.0).
// For more information see LICENSE or https://opensource.org/license/epl-2-0

package io.funktional.rumpel

import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop.*

class RandomSuite extends ScalaCheckSuite:
    private val gen =
        for
            seed <- Gen.posNum[Long]
            n    <- Gen.posNum[Int]
        yield (seed, n)

    property("nextInt always return a positive or null integer"):
        forAll(gen): (seed: Long, n: Int) =>
            val random = Random.withSeed(seed)
            n > 0 ==> random.nextInt(n)._2 >= 0

    property("nextInt always return a integer lower than the upper bound"):
        forAll(gen): (seed: Long, n: Int) =>
            val random = Random.withSeed(seed)
            n > 0 ==> (random.nextInt(n)._2 < n)
end RandomSuite
