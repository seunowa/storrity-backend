@echo off

set APP_NAME=Storrity-server
set TARGET=C:\Program Files\Storrity-server\Storrity-server.exe
set STARTUP=%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup

echo Creating startup shortcut...

REM Check if executable exists
if not exist "%TARGET%" (
    echo ❌ Executable not found at:
    echo %TARGET%
    pause
    exit /b
)

powershell -NoProfile -ExecutionPolicy Bypass -Command ^
"$WshShell = New-Object -ComObject WScript.Shell; ^
$Shortcut = $WshShell.CreateShortcut('%STARTUP%\%APP_NAME%.lnk'); ^
$Shortcut.TargetPath = '%TARGET%'; ^
$Shortcut.WorkingDirectory = 'C:\Program Files\Storrity-server'; ^
$Shortcut.IconLocation = '%TARGET%'; ^
$Shortcut.Save()"

echo ✅ Startup shortcut created successfully!
pause