#!/usr/bin/env bash
set -euo pipefail

# Gradle wrapper を実行可能にする
# (~/.gradle の所有権付け替えは devcontainer.json の onCreateCommand /
#  postStartCommand で実施しているのでここでは行わない)
chmod +x ./gradlew

# pre-commit hook を有効化 (CONTRIBUTING.md の手順と等価)
chmod +x .githooks/pre-commit
git config core.hooksPath .githooks

# Claude Code CLI を公式インストーラで導入 (npm 配布は非推奨)
curl -fsSL https://claude.ai/install.sh | bash

# Gradle 依存物をウォームアップ (初回 IDE 起動を高速化)
./gradlew --no-daemon help >/dev/null
