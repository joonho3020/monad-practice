# Nothing serious, just playing around for the sake of understanding....

- `Command` would correspond to actual ChiselTest APIs
- `FakeCommand` just describes how the `Command`s will be composed
- Can imagine how `fork`, `join` can be used to interleave `Commands` for different ports (the interpreter should automatically do the interleaving for us)
