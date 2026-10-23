# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

Java 25 + Maven template for the Cloudflight Coding Contest (CCC), solved one level at a time. There are no unit tests; correctness is checked by running a level against its example input/output.

## Commands

```sh
./run.sh            # compile and run the highest existing LevelN (mvn -q compile exec:java)
./run.sh 2          # run a specific level
./new-level.sh N [zip]   # unzip level-N.zip (project dir or ~/Downloads) into level-N/ and create LevelN.java from the stub
./zip-src.sh [N]    # pack src/, pom.xml, run.sh, README.md into level-N/solutions/source.zip (uses `jar`, not `zip`)
```

## Architecture

All code is in `src/main/java/ccc/`.

- `Main` picks the level number from the args (or the highest `ccc.LevelN` class that exists, up to 99), loads `ccc.LevelN` by reflection, so a new solver needs no registration, just the right class name and a no-arg constructor.
- The level directory is `level-N/` or, if that doesn't exist, `levelN/`. Every input file in it is solved and written to `<level dir>/solutions/`. Two naming schemes: `in_*.txt` → `out_*.txt`, and `*.in` → `*.out`.
- If the output file name already exists next to the input (the example), the result is checked with `Level.isCorrect` and the first differing line is printed on a mismatch. An exception in one file is reported as `FAILED` and doesn't stop the remaining files.
- `Level.solve(Input)` returns the whole output as a string. Override `isCorrect` when a level accepts several valid answers. The default comparison strips and normalizes line endings.
- `Input` holds the whole file as whitespace-separated tokens (`next`, `nextInt`, `nextInts(n)`, ...; `nextInt("R")` reads a unit-suffixed token like `3R` and returns 3); `lines()` gives the raw lines independently of the token position.
- `Helper` is the place for logic shared between levels, since later CCC levels usually build on earlier ones.

Each `level-N/` holds the problem statement (PDF) and inputs; read it before implementing `solve`.
