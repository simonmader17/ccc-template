package ccc;

/** One contest level: turns the contents of an input file into the expected output text. */
public interface Level {
    String solve(Input in);

    /** Whether {@code actual} is a correct answer; by default it must equal the expected output. */
    default boolean isCorrect(Input in, String expected, String actual) {
        return normalize(expected).equals(normalize(actual));
    }

    static String normalize(String s) {
        return s.strip().replace("\r\n", "\n");
    }
}
