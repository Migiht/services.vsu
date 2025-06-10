@echo off
setlocal enabledelayedexpansion

echo =================================================================
echo WARNING: This script will permanently remove comments from all
echo .java files in this directory and all subdirectories.
echo It is highly recommended to back up your project or use a
echo version control system (like Git) before proceeding.
echo.
echo This script may incorrectly remove text if '//' or '/*'
echo appears inside string literals (e.g., in a URL).
echo =================================================================
echo.
set /p "continue=Are you sure you want to continue? (y/n): "
if /i not "%continue%"=="y" (
    echo Aborting.
    exit /b
)

echo.
echo Removing comments...

rem The PowerShell command finds all .java files recursively, reads their content,
rem removes multi-line and single-line comments via regex, and writes the content back.
powershell -NoProfile -ExecutionPolicy Bypass -Command "Get-ChildItem -Path '.' -Recurse -Include *.java | ForEach-Object { try { $content = Get-Content $_.FullName -Raw; $newContent = $content -replace '/\*[\s\S]*?\*/','' -replace '//.*',''; Set-Content -Path $_.FullName -Value $newContent -Encoding UTF8 -ErrorAction Stop } catch { Write-Host 'Failed to process file:' $_.FullName -ForegroundColor Red } }"

echo.
echo Done. All comments should be removed.
pause 