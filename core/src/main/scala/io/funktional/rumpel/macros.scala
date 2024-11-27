package io.funktional.rumpel.macros

import io.funktional.rumpel.Dictionary
import io.funktional.rumpel.RumpelFormat
import scala.quoted.*

def rumpelFormat(sc: Expr[StringContext], args: Expr[Seq[Any]])(using Quotes): Expr[RumpelFormat.Sequence] =
    import quotes.reflect.*

    val parts: List[String] =
        sc match
            case '{ StringContext(${ Varargs(Exprs(parts)) }*) } => parts.toList
            case expr                                            =>
                report.errorAndAbort(s"Invalid string context: ${expr.show}")

    // The interpolated args are a list of size `parts.length - 1`. We also just know this.
    val dicts: List[Expr[Any]] =
        val Varargs(dicts) = args: @unchecked // we just know this. right?
        dicts.toList

    val constants = parts.map: part =>
        '{ RumpelFormat.Constant(${ Expr(part) }) }

    val picked = dicts.map:
        case '{ $dict: Dictionary }     => '{ RumpelFormat.Pick($dict) }
        case '{ $string: String }       => '{ RumpelFormat.Constant($string) }
        case '{ $format: RumpelFormat } => format
        case _: Expr[?]                 =>
            report.errorAndAbort("Rumpel `format` interpolator are Dictionaries, Strings or RumpelFormats")

    val formats = picked.zip(constants.tail).foldLeft(List(constants.head)):
        case (acc, (pick, constant)) => constant :: pick :: acc

    val finalized = Expr.ofList(formats.reverse)

    '{ RumpelFormat.Sequence($finalized) }
end rumpelFormat
