@echo off
echo =========================================
echo   Building Storrity Installer...
echo =========================================

"C:\Program Files\Apache NetBeans\jdk\bin\jpackage.exe" --name Storrity ^
         --input "C:\Users\Prov-Soft-Dev-1\Documents\NetBeansProjects\storrity\target" ^
         --main-jar storrity-0.0.1-SNAPSHOT.jar ^
         --type exe ^
         --runtime-image "C:\Program Files\Apache NetBeans\jdk" ^
         --win-shortcut ^
         --win-menu ^
         --win-console ^
		 --java-options "-Dspring.profiles.active=webdev" ^
         --dest "C:\Users\Prov-Soft-Dev-1\Documents\NetBeansProjects\storrity\dist"

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to create installer.
    exit /b %ERRORLEVEL%
)

echo =========================================
echo   Installer Build Completed Successfully!
echo   Output folder:
echo   C:\Users\Prov-Soft-Dev-1\Documents\NetBeansProjects\storrity\dist
echo =========================================
pause
