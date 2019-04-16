@echo off
if "%JAVA_HOME%"=="" (
set /p JAVA_HOME="JAVA_HOME: [%JAVA_HOME%]"
)
set CLASS_PATH=%~dp0\jchecksum-1.0.1904-jar-with-dependencies.jar
%JAVA_HOME%\bin\java -cp %CLASS_PATH% io.apercova.jchecksum.JCheckSum %1 %2 %3 %4 %5 %6 %7 %8 %9
