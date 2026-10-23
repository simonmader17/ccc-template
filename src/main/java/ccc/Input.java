package ccc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Whitespace-separated token reader over a whole input file. */
public final class Input {
    private final String content;
    private final String[] tokens;
    private int pos;

    public Input(String content) {
        this.content = content;
        String trimmed = content.strip();
        this.tokens = trimmed.isEmpty() ? new String[0] : trimmed.split("\\s+");
    }

    public boolean hasNext() {
        return pos < tokens.length;
    }

    public String next() {
        return tokens[pos++];
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public List<String> nextTokens(int count) {
        List<String> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(next());
        }
        return result;
    }

    public int[] nextInts(int count) {
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = nextInt();
        }
        return result;
    }

    public long[] nextLongs(int count) {
        long[] result = new long[count];
        for (int i = 0; i < count; i++) {
            result[i] = nextLong();
        }
        return result;
    }

    /** The raw file split into lines, for line-based formats; independent of the token position. */
    public List<String> lines() {
        return Arrays.asList(content.strip().split("\\R"));
    }
}
