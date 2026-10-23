package ccc;

public class Level1 implements Level {

    @Override
    public String solve(Input in) {
        // TODO: read the input (see the level's PDF) and build the output
        StringBuilder out = new StringBuilder();

        int n = in.nextInt();

        for (int i = 0; i < n; i++) {
            out.append(in.next()).append('\n');
        }

        return out.toString();
    }
}
