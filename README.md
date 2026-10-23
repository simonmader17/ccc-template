# CCC Template

A Java template for the Cloudflight Coding Contest (CCC), solved one level at a time.

## Requirements

- Java 25
- Maven

## Running

```sh
./run.sh        # run the latest level (highest existing LevelN class)
./run.sh 2      # run a specific level
```

Each run solves every input file of the level and writes the answers to `level-N/solutions/`. Both naming schemes are supported:

- `level-N/in_level-N_*.txt` → `out_level-N_*.txt`
- `levelN/levelN_*.in` → `levelN_*.out`

If an expected output file exists next to an input (the example), the result is checked against it. On a mismatch, the first differing line is printed:

```
in_level-1_0-example.txt                 3 ms  OK (matches expected)
in_level-1_1-small.txt                   1 ms  written
in_level-1_2-large.txt                  12 ms  FAILED: java.lang.ArrayIndexOutOfBoundsException: ...
```

A failure in one file doesn't stop the others.

## Layout

- `level-N/`: the level's problem statement, input files and example output
- `src/main/java/ccc/`
  - `Main`: runs a level over its input files
  - `Level`: the interface every solver implements
  - `LevelN`: the solver for level N, found automatically by name
  - `Helper`: logic shared between levels
  - `Input`: reads an input file as whitespace-separated tokens (or as lines with `lines()`)

## Adding a level

1. Run `./new-level.sh N`. It unzips `level-N.zip` (from the project folder or `~/Downloads`, or pass a path as the second argument) into `level-N/` and creates `LevelN.java` from the stub.
2. Implement `solve`. If a level accepts several valid answers, override `isCorrect` to check the rules instead.
3. Run `./run.sh` and submit the files in `level-N/solutions/`.
4. If the source code has to be uploaded, run `./zip-src.sh [N]`. It packs `src/`, `pom.xml`, `run.sh` and the README into `level-N/solutions/source.zip`.
