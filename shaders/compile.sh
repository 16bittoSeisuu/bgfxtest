#!/usr/bin/env bash

set -euo pipefail

rm -rf shaders
mkdir -p shaders/opengl
mkdir -p shaders/metal
mkdir -p shaders/vulkan

./shaderc -f vs_triangle.sc -o shaders/opengl/vs_triangle.bin \
  --type vertex -p 440 --platform windows

./shaderc -f fs_triangle.sc -o shaders/opengl/fs_triangle.bin \
  --type fragment -p 440 --platform windows

./shaderc -f vs_triangle.sc -o shaders/metal/vs_triangle.bin \
  --type vertex -p metal --platform macos

./shaderc -f fs_triangle.sc -o shaders/metal/fs_triangle.bin \
  --type fragment -p metal --platform macos

./shaderc -f vs_triangle.sc -o shaders/vulkan/vs_triangle.bin \
  --type vertex -p spirv --platform linux

./shaderc -f fs_triangle.sc -o shaders/vulkan/fs_triangle.bin \
  --type fragment -p spirv --platform linux

rm -rf ../src/main/resources/shaders
cp -r shaders ../src/main/resources/
