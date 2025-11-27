@echo off
setlocal enabledelayedexpansion

if not exist shaders\dx11 (
    mkdir shaders\dx11
)

shaderc.exe ^
  -f vs_triangle.sc ^
  -o shaders\dx11\vs_triangle.bin ^
  --type vertex ^
  --profile s_5_0 ^
  --platform windows

shaderc.exe ^
  -f fs_triangle.sc ^
  -o shaders\dx11\fs_triangle.bin ^
  --type fragment ^
  --profile s_5_0 ^
  --platform windows

pause
