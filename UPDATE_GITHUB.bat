@echo off
cd /d "%~dp0"
git add .
git commit -m "Update WestClient"
git push origin master
if errorlevel 1 git push origin main
pause

