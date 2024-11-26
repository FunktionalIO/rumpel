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
libraryDependencies += "io.funktional" %% "rumpel" % "0.1.0"
Note: Ensure the library is published to Maven Central before use.
```

### Usage
Here’s an example of how to use Rumpel:

```scala
import io.funktional.rumpel.*

object Example extends App:
  val config = RumpelConfig(
    dictionaries = List(Adjectives, Colors, Animals),
    separator = "-",
    length = 3
  )

  val rumpel = new Rumpel(config)

  println(rumpel.generate()) // Example: "brave-red-fox"
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
- Add support for Scala.js and Scala Native.
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