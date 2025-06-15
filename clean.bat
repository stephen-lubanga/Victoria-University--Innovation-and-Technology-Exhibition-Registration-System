REM ============================================
REM CLEAN.BAT - Cleanup Script
REM ============================================
@echo off
cls
echo.
echo ================================================
echo    Exhibition Registration System - Cleanup
echo ================================================
echo.

set /p confirm="Are you sure you want to delete compiled files? (Y/N): "
if /i not "%confirm%"=="Y" (
    echo Cleanup cancelled.
    pause
    exit /b 0
)

echo Cleaning up compiled files...

REM Delete class files
if exist "*.class" (
    del /q *.class
    echo Deleted .class files
)

REM Delete backup files
if exist "*~" (
    del /q *~
    echo Deleted backup files
)

REM Delete temporary files
if exist "*.tmp" (
    del /q *.tmp
    echo Deleted temporary files
)

echo.
echo Cleanup completed!
echo Source files and database are preserved.
pause