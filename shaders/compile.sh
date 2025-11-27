#!/usr/bin/env bash

set -euo pipefail

#!/usr/bin/env bash

set -euo pipefail

mkdir -p shaders/metal
./shaderc -f vs_triangle.sc -o shaders/metal/vs_triangle.bin \
  --type vertex -p metal --platform macos
./shaderc -f fs_triangle.sc -o shaders/metal/fs_triangle.bin \
  --type fragment -p metal --platform macos
