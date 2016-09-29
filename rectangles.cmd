@echo off
@REM enable echoing my setting RECTANGLES_BATCH_ECHO to 'on'
@if "%RECTANGLES_BATCH_ECHO%" == "on"  echo %RECTANGLES_BATCH_ECHO%

@setlocal

set ERROR_CODE=0

set PROJECTBASEDIR=%BASEDIR%
IF NOT "%PROJECTBASEDIR%"=="" goto endDetectBaseDir

set EXEC_DIR=%CD%
set WDIR=%EXEC_DIR%
:findBaseDir
IF EXIST "%WDIR%"\.mvn goto baseDirFound
cd ..
IF "%WDIR%"=="%CD%" goto baseDirNotFound
set WDIR=%CD%
goto findBaseDir

:baseDirFound
set PROJECTBASEDIR=%WDIR%
cd "%EXEC_DIR%"
goto endDetectBaseDir

:baseDirNotFound
set PROJECTBASEDIR=%EXEC_DIR%
cd "%EXEC_DIR%"

:endDetectBaseDir

call mvn.cmd install -pl rectangles-cli -am
cd "%PROJECTBASEDIR/rectangles-cli"

call ../mvnw.cmd spring-boot:run

exit /B %ERROR_CODE%
