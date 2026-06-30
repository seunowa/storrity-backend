@echo off

set APP_NAME=Storrity-server
set STARTUP=%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup
set SHORTCUT=%STARTUP%\%APP_NAME%.lnk

echo Removing startup shortcut...

if exist "%SHORTCUT%" (
    del "%SHORTCUT%"
    echo ✅ Startup shortcut removed successfully
) else (
    echo ⚠️ Startup shortcut not found
)

pause