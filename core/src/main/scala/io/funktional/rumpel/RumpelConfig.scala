// Copyright (c) 2024-2024 by Raphaël Lemaitre and Contributors
// This software is licensed under the Eclipse Public License v2.0 (EPL-2.0).
// For more information see LICENSE or https://opensource.org/license/epl-2-0

package io.funktional.rumpel

/**
 * Configuration class for the Rumpel generator.
 *
 * @param dictionaries a list of dictionaries to be used for generating words
 * @param separator the separator to be used between generated words
 * @param length the number of words to generate
 * @param style the style to be applied to the generated words
 * @param seed an optional seed for the random number generator
 */
case class RumpelConfig(
    dictionaries: List[Dictionary],
    separator: String = "_",
    length: Int = 3,
    style: RumpelConfig.Style = RumpelConfig.Style.LowerCase,
    seed: Option[Long] = None
)

object RumpelConfig:
    /**
     * Enumeration representing the style to be applied to the generated words.
     */
    enum Style:
        case LowerCase
        case UpperCase
        case Capitalize
    end Style

    /**
     * Extension methods for the Style enumeration.
     */
    extension (style: Style)
        /**
         * Formats a string according to the style.
         *
         * @param value the string to be formatted
         * @return the formatted string
         */
        def format(value: String): String = style match
            case Style.LowerCase  => value.toLowerCase
            case Style.UpperCase  => value.toUpperCase
            case Style.Capitalize => value.capitalize
end RumpelConfig
