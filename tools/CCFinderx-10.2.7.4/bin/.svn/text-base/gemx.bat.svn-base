@echo off

if not "%1" == "" (
set OPEN_CLONEDATAFILE=-open "%~f1"
)

rem set CCFINDERX_PYTHON_INTERPRETER_PATH=C:\Python26\python.exe
set CCFINDERX_TEMPORARY_DIRECTORY=%TEMP%

rem set up environment variables for Python
rem set PYTHONHOME=
rem set PYTHONPATH=
rem set PYTHONSTARTUP=

set THE_JARS=gemxlib.jar;pathjson.jar;trove-2.0.4.jar;icu4j-4_0_1.jar;icu4j-charsets-4_0_1.jar;icu4j-localespi-4_0_1.jar;swt.jar;.

pushd "%~dp0"

java -classpath %THE_JARS% GemXMain %OPEN_CLONEDATAFILE%

rem use below if you want to hide the console window
rem start javaw -classpath %THE_JARS% GemXMain %OPEN_CLONEDATAFILE%
:DONE

if ERRORLEVEL 1 goto REPORT_ERROR
popd
goto :EOF

:REPORT_ERROR
popd
pause
