@echo off
if "%JAVA_HOME%"=="" (
set /p JAVA_HOME="JAVA_HOME: [%JAVA_HOME%]"
)
set CLASS_PATH=%~dp0\jchecksum-0.0.1-jar-with-dependencies.jar
%JAVA_HOME%\bin\java -cp %CLASS_PATH% io.apercova.jchecksum.CheckSum %1 %2 %3 %4 %5 %6 %7 %8 %9
