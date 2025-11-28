#!/usr/bin/env bash

set -euo pipefail

mkdir -p shaders/spirv

./shaderc -f vs_triangle.sc -o shaders/spirv/vs_triangle.bin \
  --type vertex -p spirv --platform linux

./shaderc -f fs_triangle.sc -o shaders/spirv/fs_triangle.bin \
  --type fragment -p spirv --platform linux
