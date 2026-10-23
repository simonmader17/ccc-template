#!/usr/bin/env bash
# Usage: ./run.sh [N]   (default: the highest existing LevelN)
set -euo pipefail
cd "$(dirname "$0")"
mvn -q compile exec:java -Dexec.args="${1:-}"
