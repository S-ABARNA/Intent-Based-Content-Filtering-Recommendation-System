@echo off
echo ==========================================
echo YouTube Content Filter GUI - Compilation
echo ==========================================
echo.

if not exist bin mkdir bin

echo Compiling Java files...
dir /s /B src\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    echo Please check your Java files for errors.
    pause
    exit /b %errorlevel%
)

echo.
echo [SUCCESS] Compilation completed!
echo.
echo ==========================================
echo Starting YouTube Content Filter GUI...
echo ==========================================
echo.
java -cp bin com.youtubefilter.MainGUI

pause