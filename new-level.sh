#!/usr/bin/env bash
# Usage: ./new-level.sh N [path/to/level-N.zip]
# Unzips the level into level-N/ and creates src/main/java/ccc/LevelN.java from Level1's stub.
set -euo pipefail
cd "$(dirname "$0")"

n="${1:?usage: ./new-level.sh N [zip]}"
zip="${2:-}"
if [[ -z "$zip" ]]; then
	for candidate in "level-$n.zip" "level$n.zip" "$HOME/Downloads/level-$n.zip" "$HOME/Downloads/level$n.zip"; do
		if [[ -f "$candidate" ]]; then zip="$candidate"; break; fi
	done
fi

if [[ -n "$zip" ]]; then
	mkdir -p "level-$n"
	unzip -o -q "$zip" -d "level-$n"
	echo "Unzipped $zip into level-$n/"
else
	echo "No zip found for level $n, skipping unzip"
fi

class="src/main/java/ccc/Level$n.java"
if [[ -e "$class" ]]; then
	echo "$class already exists"
else
	cat > "$class" <<EOF
package ccc;

public class Level$n implements Level {

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
EOF
	echo "Created $class"
fi
