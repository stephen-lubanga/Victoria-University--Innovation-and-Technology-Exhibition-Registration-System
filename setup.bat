REM ============================================
REM SETUP.BAT - Initial Setup Script
REM ============================================
@echo off
cls
echo.
echo ================================================
echo    Exhibition Registration System - Setup
echo ================================================
echo.

echo This script will help you set up the project structure.
echo.

REM Create lib directory if it doesn't exist
if not exist "lib" (
    echo Creating lib directory...
    mkdir lib
    echo lib directory created.
) else (
    echo lib directory already exists.
)

REM Create empty database file if it doesn't exist
if not exist "VUE_Exhibition.accdb" (
    echo.
    echo Creating empty database file...
    echo. > VUE_Exhibition.accdb
    echo VUE_Exhibition.accdb created.
    echo NOTE: This is just an empty file. If you have MS Access,
    echo       please create a proper .accdb file instead.
) else (
    echo VUE_Exhibition.accdb already exists.
)

echo.
echo ================================================
echo    SETUP INSTRUCTIONS
echo ================================================
echo.
echo 1. Download UCanAccess from: http://ucanaccess.sourceforge.net
echo 2. Extract the ZIP file
echo 3. Copy these JAR files to the lib directory:
echo    - ucanaccess-X.X.X.jar
echo    - commons-lang3-X.X.X.jar
echo    - commons-logging-X.X.jar
echo    - hsqldb-X.X.X.jar
echo    - jackcess-X.X.X.jar
echo.
echo 4. Make sure ExhibitionRegistrationGUI.java is in this directory
echo 5. Run compile.bat to compile the program
echo 6. Run run.bat to start the application
echo.

echo Current directory contents:
dir /b
echo.

if exist "lib" (
    echo Contents of lib directory:
    dir /b lib
    echo.
)

echo Setup completed!
pause