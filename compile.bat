REM ============================================
REM COMPILE.BAT - Compilation Script
REM ============================================
@echo off
cls
echo.
echo ================================================
echo    Exhibition Registration System - Compiler
echo ================================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH!
    echo Please install Java JDK 8 or higher.
    pause
    exit /b 1
)

REM Check if source file exists
if not exist "ExhibitionRegistrationSystem.java" (
    echo ERROR: ExhibitionRegistrationGUI.java not found!
    echo Make sure you're in the correct directory.
    pause
    exit /b 1
)

REM Check if lib directory exists
if not exist "lib" (
    echo ERROR: lib directory not found!
    echo Please create lib directory and add UCanAccess JAR files.
    pause
    exit /b 1
)

REM Check if JAR files exist
set "missing_jars="
if not exist "lib\ucanaccess*.jar" set "missing_jars=%missing_jars% ucanaccess"
if not exist "lib\commons-lang3*.jar" set "missing_jars=%missing_jars% commons-lang3"
if not exist "lib\commons-logging*.jar" set "missing_jars=%missing_jars% commons-logging"
if not exist "lib\hsqldb*.jar" set "missing_jars=%missing_jars% hsqldb"
if not exist "lib\jackcess*.jar" set "missing_jars=%missing_jars% jackcess"

if not "%missing_jars%"=="" (
    echo ERROR: Missing JAR files: %missing_jars%
    echo Please download UCanAccess and copy all JAR files to lib directory.
    pause
    exit /b 1
)

echo Compiling ExhibitionRegistrationSystem.java...
echo.

REM Compile the Java file
javac -cp "lib\*;." ExhibitionRegistrationSystem.java

REM Check compilation result
if %errorlevel% equ 0 (
    echo.
    echo ================================================
    echo    COMPILATION SUCCESSFUL! 
    echo ================================================
    echo.
    echo Your program has been compiled successfully.
    echo You can now run it using run.bat
    echo.
) else (
    echo.
    echo ================================================
    echo    COMPILATION FAILED! 
    echo ================================================
    echo.
    echo Please check the error messages above.
    echo Make sure all required JAR files are in lib directory.
    echo.
)

pause