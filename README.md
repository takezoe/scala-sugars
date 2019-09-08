scala-sugars [![Actions Status](https://github.com/takezoe/scala-sugars/workflows/CI/badge.svg)](https://github.com/takezoe/scala-sugars/actions)
========

Some useful syntax sugars for Scala.

```scala
import com.github.takezoe.sugars._
```

## Control syntax

```scala
// returns a last value as Option (None if exception is caused)
ignoreException {
  ...
}

// returns a last value
defining(user.firstName + " " + user.lastName){ fullNanme =>
  ...
}
```

## Loan pattern

```scala
// stream is closed automatically
using(new FileInputStream(file)){ in =>
  ...
}

// uses two resource at the same time
using(new FileInputStream(file1),
      new FileOutputStream(file2)){ (in, out) =>
  ...
}
```

## Union type

```scala
def test(arg: String | Int | Boolean): Unit = {
  arg.value match {
    case x: String  => println("String")
    case x: Int     => println("Int")
    case x: Boolean => println("Boolean")
  }
}

test("str")
test(123)
test(true)
test(Some("str")) // => compile error
```

## Convenient methods

```scala
// unsafeTap
val conn = getConnection() <| { _.setAutoCommit(false) }

// type-safe comparison
"apple" === "apple"       // => true
"apple" === "orange"      // => false
Some("apple") === "apple" // => compile error
```
