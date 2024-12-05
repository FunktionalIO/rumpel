# Rumpel

**Rumpel** is a Scala 3 library for generating unique, human-readable names. Inspired by the mythological figure Rumpelstiltskin, who wove straw into gold, this library weaves random words into memorable names. It’s a Scala port of the popular [unique-names-generator](https://github.com/andreasonny83/unique-names-generator).

## Features

- Fully compatible with **Scala 3**.
- Generate unique names using customizable dictionaries and configurations.
- Control separators, styles, and length of the generated names.
- Lightweight, efficient, and idiomatic to Scala.
- Open source and maintained by the **io.funktional** organization.

## Getting Started

### Installation

Add the following dependency to your `build.sbt`:

```scala
libraryDependencies += "io.funktional" %% "rumpel" % "<version>"
```
If you are compiling for non-JVM targets, you can use the cross-versioned dependency:

```scala
libraryDependencies += "io.funktional" %%% "rumpel" % "<version>"
```

### Usage
Here’s an example of how to use Rumpel:

```scala
import io.funktional.rumpel.*
import io.funktional.rumpel.dictionaries.Adjectives
import io.funktional.rumpel.dictionaries.Animals
import io.funktional.rumpel.dictionaries.Colors

object Example extends App:
  val config = RumpelConfig(
    dictionaries = List(Adjectives, Colors, Animals),
    separator = "-",
    length = 3
  )

  val rumpel = new Rumpel(config)

  println(rumpel.generate()) // Example: "brave-red-fox"
```

It is possible to use a custom random number generator by providing an instance of `io.funktional.rumpel.Random`:

```scala
object MyRandom extends io.funktional.rumpel.Random:
    def nextInt(n: Int): (Random, Int) =
      ??? // Implement your own logic here
    def shuffle[T](elements: List[T]): (Random, List[T]) =
      ??? // Implement your own logic here
    def pick[T](list: List[T], n: Int): (Random, List[T]) =
      ??? // Implement your own logic here

object Example extends App:
  val config = RumpelConfig(
    dictionaries = List(Adjectives, Colors, Animals),
    separator = "-",
    length = 3
  )

  val rumpel = new Rumpel(config, MyRandom)

  println(rumpel.generate()) // Example: "brave-red-fox"
```

Alternatively, you can also use the `rumpel` interpolator to customize more complex format.
Casing changes and separator tuning are not yet supported using this mode, so be careful of the generators you use !

```scala
import io.funktional.rumpel.*
import io.funktional.rumpel.dictionaries.*

val identifier = rumpel"$Adjectives.$Animals"

// you can then use the `.generate` method with a given random in scope
println(identifier.generate(using Random.default)) // Exemple: "brave.fox"

// The interpolator can also accept raw strings and even other RumpelFormat objects !
def fakeEmail(domain: String)(using Random): String =
  rumpel"$identifier@$domain.$Colors".generate

println(fakeEmail("yahoo")(using Random.default)) // Exemple: "brave.fox@yahoo.blue"
```

### Configuration Options
You can customize Rumpel by providing a `RumpelConfig`:

- dictionaries: A list of word dictionaries to draw from (e.g., adjectives, animals, colors).
- separator: A string used to separate words (default: "-").
- length: The number of words in the generated name (default: 3).

### Built-in Dictionaries

The library includes built-in dictionaries for:

- Adjectives: e.g., "brave", "calm", "diligent".
- Colors: e.g., "red", "blue", "green".
- Animals: e.g., "fox", "bear", "eagle".

You can also add your own custom dictionaries.

## Roadmap
- Expand built-in dictionaries.
- Introduce advanced customization features.
- Provide integration examples with popular Scala frameworks.

## Contributing
Contributions are welcome! Here’s how to get started:

1. Fork the repository.
2. Create a new branch: git checkout -b feature-name.
3. Make your changes and commit: git commit -m "Add feature".
4. Push the branch: git push origin feature-name.
5. Open a pull request.

### Building Locally
To build Rumpel locally, clone the repository and use sbt:

```bash
git clone https://github.com/io.funktional/rumpel.git
cd rumpel
sbt compile
```

Run tests with:

```bash
sbt test
```

## License
This project is licensed under the [Eclipse Public License - v 2.0](https://www.eclipse.org/legal/epl-2.0/).

## Acknowledgments

Rumpel is inspired by the [unique-names-generator](https://github.com/andreasonny83/unique-names-generator) library and brings its functionality into the Scala ecosystem.