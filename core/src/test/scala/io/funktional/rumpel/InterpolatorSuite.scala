// Copyright (c) 2024-2024 by Raphaël Lemaitre and Contributors
// This software is licensed under the Eclipse Public License v2.0 (EPL-2.0).
// For more information see LICENSE or https://opensource.org/license/epl-2-0

package io.funktional.rumpel

import munit.FunSuite

class InterpolatorSuite extends FunSuite:
    val foo = MockDictionary(List("foo"))
    val bar = MockDictionary(List("bar"))
    val baz = MockDictionary(List("baz"))

    test("interpolation: using dictionaries") {
        assertEquals(rumpel"${foo}_${bar}_$baz".generate(using Random.default), "foo_bar_baz")
    }

    val raw = "some_very_raw_string"

    test("interpolation: using strings") {
        assertEquals(rumpel"$raw@constant".generate(using Random.default), "some_very_raw_string@constant")
    }

    val nested = rumpel"${foo}_${bar}_$baz"

    test("interpolation: nested") {
        assertEquals(rumpel"$nested@constant".generate(using Random.default), "foo_bar_baz@constant")
    }

    test("interpolation: all together") {
        assertEquals(rumpel"$nested@$foo.$raw".generate(using Random.default), "foo_bar_baz@foo.some_very_raw_string")
    }

end InterpolatorSuite
