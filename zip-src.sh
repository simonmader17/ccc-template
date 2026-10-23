#!/usr/bin/env bash
# Usage: ./zip-src.sh [N]   (default: the highest existing LevelN)
# Packs the source code into level-N/solutions/source.zip for uploading.
set -euo pipefail
cd "$(dirname "$0")"

n="${1:-$(ls src/main/java/ccc/ | sed -n 's/^Level\([0-9]\+\)\.java$/\1/p' | sort -n | tail -1)}"
if [[ -z "$n" ]]; then
	echo "No LevelN class found" >&2
	exit 1
fi

dir="level-$n"
[[ -d "$dir" || ! -d "level$n" ]] || dir="level$n"
mkdir -p "$dir/solutions"
out="$dir/solutions/source.zip"
rm -f "$out"

# jar ships with the JDK, so this works without zip installed; M = no manifest. Hidden files are skipped.
mapfile -t sources < <(find src -type f ! -path '*/.*')
jar -cMf "$out" "${sources[@]}" pom.xml run.sh README.md
echo "Created $out"
