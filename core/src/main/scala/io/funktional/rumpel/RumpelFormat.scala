// Copyright (c) 2024-2024 by Raphaël Lemaitre and Contributors
// This software is licensed under the Eclipse Public License v2.0 (EPL-2.0).
// For more information see LICENSE or https://opensource.org/license/epl-2-0

package io.funktional.rumpel

enum RumpelFormat:
    case Constant(value: String)
    case Pick(dictionary: Dictionary)
    case Sequence(parts: List[RumpelFormat])

    private def format(random: Random): (Random, String) = this match
        case Constant(value)  => (random, value)
        case Pick(dictionary) => dictionary.pickOne(random)
        case Sequence(parts)  =>
            val builder = StringBuilder()
            var rng     = random
            parts.foreach: part =>
                val (newRng, value) = part.format(rng)
                rng = newRng
                builder.append(value)
            (rng, builder.toString)

    def generate(using random: Random): String = format(random)(1)

end RumpelFormat

extension (inline sc: StringContext)
    transparent inline def rumpel(inline args: Any*): RumpelFormat =
        ${ macros.rumpelFormat('sc, 'args) }

end extension
