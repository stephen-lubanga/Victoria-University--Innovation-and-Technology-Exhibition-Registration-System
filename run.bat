REM ============================================
REM RUN.BAT - Execution Script
REM ============================================
@echo off
cls
echo.
echo ================================================
echo    Exhibition Registration System - Runner
echo ================================================
echo.

REM Check if compiled class exists
if not exist "ExhibitionRegistrationSystem.class" (
    echo ERROR: ExhibitionRegistrationSystem.class not found!
    echo Please run compile.bat first to compile the program.
    echo.
    set /p compile_now="Would you like to compile now? (Y/N): "
    if /i "%compile_now%"=="Y" (
        call compile.bat
        if %errorlevel% neq 0 (
            echo Compilation failed. Cannot run the program.
            pause
            exit /b 1
        )
    ) else (
        pause
        exit /b 1
    )
)

REM Check if database file exists
if not exist "VUE_Exhibition.accdb" (
    echo WARNING: VUE_Exhibition.accdb not found!
    echo The program will try to create it automatically.
    echo If you have Access installed, create an empty database file first.
    echo.
    pause
)

echo Starting Exhibition Registration System...
echo.
echo ================================================
echo    LAUNCHING APPLICATION...
echo ================================================
echo.
echo If the application doesn't start, check:
echo 1. All JAR files are in lib directory
echo 2. VUE_Exhibition.accdb exists (can be empty)
echo 3. Java is properly installed
echo.

REM Run the Java application
java -cp "lib\*;." ExhibitionRegistrationSystem

REM Check if program ran successfully
if %errorlevel% equ 0 (
    echo.
    echo ================================================
    echo    APPLICATION CLOSED SUCCESSFULLY
    echo ================================================
) else (
    echo.
    echo ================================================
    echo    APPLICATION ENCOUNTERED AN ERROR
    echo ================================================
    echo.
    echo Common solutions:
    echo 1. Make sure VUE_Exhibition.accdb exists
    echo 2. Check if all JAR files are in lib directory
    echo 3. Verify Java version is 8 or higher
    echo.
)

pause