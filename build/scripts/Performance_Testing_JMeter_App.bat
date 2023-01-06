@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Performance_Testing_JMeter_App startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and PERFORMANCE_TESTING_J_METER_APP_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\Performance_Testing_JMeter_App.jar;%APP_HOME%\lib\ApacheJMeter_core-5.4.1.jar;%APP_HOME%\lib\jorphan-5.4.1.jar;%APP_HOME%\lib\commons-math3-3.6.1.jar;%APP_HOME%\lib\java-client-7.5.1.jar;%APP_HOME%\lib\selenium-java-3.141.59.jar;%APP_HOME%\lib\selenium-chrome-driver-3.141.59.jar;%APP_HOME%\lib\selenium-edge-driver-3.141.59.jar;%APP_HOME%\lib\selenium-firefox-driver-3.141.59.jar;%APP_HOME%\lib\selenium-ie-driver-3.141.59.jar;%APP_HOME%\lib\selenium-opera-driver-3.141.59.jar;%APP_HOME%\lib\selenium-safari-driver-3.141.59.jar;%APP_HOME%\lib\selenium-support-3.141.59.jar;%APP_HOME%\lib\selenium-remote-driver-3.141.59.jar;%APP_HOME%\lib\guava-25.0-jre.jar;%APP_HOME%\lib\selenium-api-3.141.59.jar;%APP_HOME%\lib\byte-buddy-1.8.15.jar;%APP_HOME%\lib\commons-exec-1.3.jar;%APP_HOME%\lib\okhttp-3.11.0.jar;%APP_HOME%\lib\okio-1.14.0.jar;%APP_HOME%\lib\gson-2.8.6.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\cglib-3.3.0.jar;%APP_HOME%\lib\commons-validator-1.7.jar;%APP_HOME%\lib\commons-text-1.9.jar;%APP_HOME%\lib\commons-lang3-3.12.0.jar;%APP_HOME%\lib\xmlgraphics-commons-2.3.jar;%APP_HOME%\lib\commons-io-2.8.0.jar;%APP_HOME%\lib\spring-context-5.3.4.jar;%APP_HOME%\lib\aspectjweaver-1.9.6.jar;%APP_HOME%\lib\log4j-slf4j-impl-2.13.3.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.30.jar;%APP_HOME%\lib\slf4j-api-1.7.30.jar;%APP_HOME%\lib\ApacheJMeter-5.4.1.jar;%APP_HOME%\lib\bsf-2.4.0.jar;%APP_HOME%\lib\rsyntaxtextarea-3.1.1.jar;%APP_HOME%\lib\jtidy-r938.jar;%APP_HOME%\lib\xstream-1.4.15.jar;%APP_HOME%\lib\log4j-1.2-api-2.13.3.jar;%APP_HOME%\lib\log4j-core-2.13.3.jar;%APP_HOME%\lib\log4j-api-2.13.3.jar;%APP_HOME%\lib\apiguardian-api-1.1.0.jar;%APP_HOME%\lib\oro-2.0.8.jar;%APP_HOME%\lib\xalan-2.7.2.jar;%APP_HOME%\lib\Saxon-HE-9.9.1-8.jar;%APP_HOME%\lib\jackson-databind-2.10.5.jar;%APP_HOME%\lib\jackson-annotations-2.10.5.jar;%APP_HOME%\lib\jackson-core-2.10.5.jar;%APP_HOME%\lib\darklaf-core-2.5.4.jar;%APP_HOME%\lib\darklaf-windows-2.5.4.jar;%APP_HOME%\lib\darklaf-macos-2.5.4.jar;%APP_HOME%\lib\darklaf-theme-2.5.4.jar;%APP_HOME%\lib\darklaf-property-loader-2.5.4.jar;%APP_HOME%\lib\svgSalamander-1.1.2.3.jar;%APP_HOME%\lib\caffeine-2.8.8.jar;%APP_HOME%\lib\darklaf-extensions-rsyntaxarea-0.3.4.jar;%APP_HOME%\lib\miglayout-swing-5.2.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\commons-beanutils-1.9.4.jar;%APP_HOME%\lib\commons-collections-3.2.2.jar;%APP_HOME%\lib\commons-collections4-4.4.jar;%APP_HOME%\lib\tika-core-1.24.1.jar;%APP_HOME%\lib\freemarker-2.3.30.jar;%APP_HOME%\lib\jodd-props-5.0.13.jar;%APP_HOME%\lib\jodd-core-5.0.13.jar;%APP_HOME%\lib\rhino-1.7.13.jar;%APP_HOME%\lib\groovy-dateutil-3.0.7.jar;%APP_HOME%\lib\groovy-datetime-3.0.7.jar;%APP_HOME%\lib\groovy-jmx-3.0.7.jar;%APP_HOME%\lib\groovy-json-3.0.7.jar;%APP_HOME%\lib\groovy-jsr223-3.0.7.jar;%APP_HOME%\lib\groovy-sql-3.0.7.jar;%APP_HOME%\lib\groovy-templates-3.0.7.jar;%APP_HOME%\lib\groovy-xml-3.0.7.jar;%APP_HOME%\lib\groovy-3.0.7.jar;%APP_HOME%\lib\tika-parsers-1.24.1.jar;%APP_HOME%\lib\xercesImpl-2.12.0.jar;%APP_HOME%\lib\serializer-2.7.2.jar;%APP_HOME%\lib\xml-apis-1.4.01.jar;%APP_HOME%\lib\jsr305-1.3.9.jar;%APP_HOME%\lib\checker-compat-qual-2.0.0.jar;%APP_HOME%\lib\error_prone_annotations-2.4.0.jar;%APP_HOME%\lib\j2objc-annotations-1.1.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.14.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\asm-7.1.jar;%APP_HOME%\lib\commons-digester-2.1.jar;%APP_HOME%\lib\spring-aop-5.3.4.jar;%APP_HOME%\lib\spring-beans-5.3.4.jar;%APP_HOME%\lib\spring-expression-5.3.4.jar;%APP_HOME%\lib\spring-core-5.3.4.jar;%APP_HOME%\lib\xmlpull-1.1.3.1.jar;%APP_HOME%\lib\xpp3_min-1.1.4c.jar;%APP_HOME%\lib\checker-qual-3.8.0.jar;%APP_HOME%\lib\darklaf-utils-2.5.4.jar;%APP_HOME%\lib\darklaf-native-utils-2.5.4.jar;%APP_HOME%\lib\darklaf-platform-base-2.5.4.jar;%APP_HOME%\lib\jxlayer-3.0.4.jar;%APP_HOME%\lib\miglayout-core-5.2.jar;%APP_HOME%\lib\spring-jcl-5.3.4.jar;%APP_HOME%\lib\jna-5.5.0.jar

@rem Execute Performance_Testing_JMeter_App
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PERFORMANCE_TESTING_J_METER_APP_OPTS%  -classpath "%CLASSPATH%" performance_testing_app.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable PERFORMANCE_TESTING_J_METER_APP_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%PERFORMANCE_TESTING_J_METER_APP_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
