#!/bin/bash

# Directorio del repositorio
REPO_DIR=$(git rev-parse --show-toplevel)

# Directorio de los git hooks
HOOKS_DIR="$REPO_DIR/hooks"

# Crear enlaces simbólicos a los git hooks
ln -s -f "$HOOKS_DIR/pre-commit" "$REPO_DIR/.git/hooks/pre-commit"

# Hacer que el pre-commit sea ejecutable
chmod +x "$REPO_DIR/.git/hooks/pre-commit"
