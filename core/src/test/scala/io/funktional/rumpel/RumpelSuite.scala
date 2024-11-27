// Copyright (c) 2024-2024 by Raphaël Lemaitre and Contributors
// This software is licensed under the Eclipse Public License v2.0 (EPL-2.0).
// For more information see LICENSE or https://opensource.org/license/epl-2-0

package io.funktional.rumpel

import munit.FunSuite

class RumpelSuite extends FunSuite:

    private val dic1 = MockDictionary(List("one"))
    private val dic2 = MockDictionary(List("two"))
    private val dic3 = MockDictionary(List("three"))

    test("generate should return a string with the correct number of words"):
        val config = RumpelConfig(List(dic1, dic2, dic3))
        val rumpel = Rumpel(config)
        val result = rumpel.generate
        assertEquals(result, "one_two_three")

    test("generate should apply the correct style to the words"):
        val config = RumpelConfig(
          dictionaries = List(dic1, dic2, dic3),
          style = RumpelConfig.Style.UpperCase
        )
        val rumpel = Rumpel(config)
        val result = rumpel.generate
        assertEquals(result, "ONE_TWO_THREE")

    test("generate should use the correct separator"):
        val config = RumpelConfig(
          dictionaries = List(dic1, dic2, dic3),
          separator = "-"
        )
        val rumpel = Rumpel(config)
        val result = rumpel.generate
        assertEquals(result, "one-two-three")

    test("generate should handle an empty dictionary list"):
        val config = RumpelConfig(dictionaries = List.empty)
        val rumpel = Rumpel(config)
        val result = rumpel.generate
        assert(result.isEmpty)

    test("generate should handle a single dictionary with one word"):
        val config = RumpelConfig(
          dictionaries = List(dic1),
          length = 1
        )
        val rumpel = Rumpel(config)
        val result = rumpel.generate
        assert(result == "one")

    test("generate should be able to pick two dictionary from three"):
        val config = RumpelConfig(
          dictionaries = List(dic1, dic2, dic3),
          length = 2
        )
        val rumpel = Rumpel(config)
        val result = rumpel.generate
        assert(
          result == "one_two" || result == "one_three" || result == "two_three",
          s"result ($result) should be one of (\"one_two\", \"one_three\", \"two_three\")"
        )
end RumpelSuite
