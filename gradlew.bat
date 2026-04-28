@echo off
set APP_HOME=%~dp0
set JAVA_EXE=java.exe
set DEFAULT_JVM_OPTS=-Xmx64m -Xms64m
set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% -jar "%CLASSPATH%" %*