// Copyright (c) 2024-2024 by Raphaël Lemaitre and Contributors
// This software is licensed under the Eclipse Public License v2.0 (EPL-2.0).
// For more information see LICENSE or https://opensource.org/license/epl-2-0

package io.funktional.rumpel.dictionaries

import io.funktional.rumpel.Dictionary

/**
 * Dictionary containing a list of colors the name generator can use
 */

case object Colors extends Dictionary.ListBased:
    override protected def words: List[String] =
        List(
          "amaranth",
          "amber",
          "amethyst",
          "apricot",
          "aqua",
          "aquamarine",
          "azure",
          "beige",
          "black",
          "blue",
          "blush",
          "bronze",
          "brown",
          "chocolate",
          "coffee",
          "copper",
          "coral",
          "crimson",
          "cyan",
          "emerald",
          "fuchsia",
          "gold",
          "gray",
          "green",
          "harlequin",
          "indigo",
          "ivory",
          "jade",
          "lavender",
          "lime",
          "magenta",
          "maroon",
          "moccasin",
          "olive",
          "orange",
          "peach",
          "pink",
          "plum",
          "purple",
          "red",
          "rose",
          "salmon",
          "sapphire",
          "scarlet",
          "silver",
          "tan",
          "teal",
          "tomato",
          "turquoise",
          "violet",
          "white",
          "yellow"
        )
end Colors
