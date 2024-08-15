package playing

object Command {
  def peek(port: String) = println(s"peek ${port}")
  def poke(port: String, bits: String) = println(s"poke ${port} ${bits}")
  def step(n: Int)  = println(s"step ${n}")
}

// Command Monad
// By using FakeCommand, we can "describe" how we want to execute the command.
sealed trait FakeCommand[A]:
  def flatMap[B](f: A => FakeCommand[B]): FakeCommand[B] =
    this match
      case Result(r) => f(r)
      case fc: FakeCommand[A] => Chain(fc, f)
  def map[B](f: A => B): FakeCommand[B] = flatMap(r => Result(f(r)))

// Result of a FakeCommand
case class Result[T](r: T) extends FakeCommand[T]

// Get the return value of the FakeCommand and use it as an input to the next FakeCommand
case class Chain[A, B](fc: FakeCommand[A], f: A => FakeCommand[B]) extends FakeCommand[B]

// FakeCommand operations
case class Peek(port: String) extends FakeCommand[Result[String]]
case class Poke(port: String, bits: String) extends FakeCommand[Result[Unit]]
case class Step(n: Int) extends FakeCommand[Result[Unit]]

def interpret[A](cmds: FakeCommand[A]): A  =
  cmds match
    case Peek(port) =>
      Command.peek(port)
      Result("peek result")
    case Poke(port, bits) =>
      Result(Command.poke(port, bits))
    case Step(n) =>
      Result(Command.step(n))
    case Result(r) =>
      r
    case Chain(fc, f) =>
      /// ?????????????????
      val ret = interpret(fc)
      interpret(f(ret))

object Main extends App:
// Peek("io_a").flatMap( _ =>
//  Poke("io_b", "3")
// )
// The above expands to:
// Chain (Result[String], ResultString => Poke("io_b", "3"))
//
  val cmds =
    Peek("io_a").flatMap( _ =>
      Poke("io_b", "3").flatMap( _ =>
          Step(2).map(_ => println("Done"))))
  interpret(cmds)

  val cmds2 = for {
    _ <- Peek("io_a")
    _ <- Poke("io_b", "3")
    _ <- Step(2)
  } yield()

  interpret(cmds2)
