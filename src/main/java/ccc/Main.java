package ccc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Runs a level against every input file in its directory and writes the results to
 * {@code <level dir>/solutions/}. Where an expected output file exists (the example), the result
 * is compared against it.
 *
 * <p>Both CCC naming schemes are supported: {@code level-N/in_level-N_*.txt} → {@code out_...txt}
 * and {@code levelN/levelN_*.in} → {@code levelN_*.out}.
 *
 * <p>Usage: {@code ./run.sh [N]}. Without {@code N}, the highest existing {@code LevelN} class runs.
 */
public class Main {

    private static final int MAX_LEVEL = 99;

    public static void main(String[] args) throws IOException {
        int levelNumber = args.length > 0 && !args[0].isBlank() ? Integer.parseInt(args[0].strip()) : latestLevel();
        Level level = loadLevel(levelNumber);
        if (level == null) {
            throw new IllegalArgumentException("No class ccc.Level" + levelNumber + " found");
        }

        Path dir = levelDir(levelNumber);
        Path solutions = dir.resolve("solutions");
        Files.createDirectories(solutions);

        List<Path> inputs;
        try (Stream<Path> files = Files.list(dir)) {
            inputs = files.filter(p -> outputName(p) != null).sorted().toList();
        }
        if (inputs.isEmpty()) {
            throw new IllegalStateException("No input files found in " + dir.toAbsolutePath());
        }

        for (Path input : inputs) {
            String outName = outputName(input);
            String inputText = Files.readString(input);
            long start = System.nanoTime();
            String status;
            try {
                String result = level.solve(new Input(inputText));
                Files.writeString(solutions.resolve(outName), result);

                Path expected = dir.resolve(outName);
                status = "written";
                if (Files.exists(expected)) {
                    String expectedText = Files.readString(expected);
                    status = level.isCorrect(new Input(inputText), expectedText, result)
                            ? "OK (matches expected)"
                            : "MISMATCH with expected" + firstDifference(expectedText, result);
                }
            } catch (Exception e) {
                status = "FAILED: " + e;
            }
            long millis = (System.nanoTime() - start) / 1_000_000;
            System.out.printf("%-35s %6d ms  %s%n", input.getFileName(), millis, status);
        }
    }

    private static int latestLevel() {
        for (int n = MAX_LEVEL; n >= 1; n--) {
            if (loadLevel(n) != null) {
                return n;
            }
        }
        throw new IllegalStateException("No LevelN class found");
    }

    private static Level loadLevel(int n) {
        try {
            return (Level) Class.forName("ccc.Level" + n).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot instantiate ccc.Level" + n, e);
        }
    }

    private static Path levelDir(int n) {
        Path dashed = Path.of("level-" + n);
        return Files.isDirectory(dashed) ? dashed : Path.of("level" + n);
    }

    /** The output file name for an input file, or {@code null} if it is not an input file. */
    private static String outputName(Path input) {
        String name = input.getFileName().toString();
        if (name.startsWith("in_") && name.endsWith(".txt")) {
            return "out_" + name.substring(3);
        }
        if (name.endsWith(".in")) {
            return name.substring(0, name.length() - 3) + ".out";
        }
        return null;
    }

    private static String firstDifference(String expected, String actual) {
        String[] exp = Level.normalize(expected).split("\n", -1);
        String[] act = Level.normalize(actual).split("\n", -1);
        for (int i = 0; i < Math.max(exp.length, act.length); i++) {
            String e = i < exp.length ? exp[i] : "<missing>";
            String a = i < act.length ? act[i] : "<missing>";
            if (!e.strip().equals(a.strip())) {
                return String.format("%n    line %d%n    expected: %s%n    actual:   %s", i + 1, e, a);
            }
        }
        return "";
    }
}
