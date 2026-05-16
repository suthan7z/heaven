@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2017 Apache Software Foundation
@REM
@REM
if not "%MAVEN_HOME%"=="" goto have_maven_home
for /f "delims=" %%i in ('cd /d "%~dp0.." ^&^& cd') do set "MAVEN_HOME=%%i"
:have_maven_home

if "%JAVA_HOME%"=="" (
  for /f "tokens=*" %%i in ('where java.exe 2^>nul') do set "JAVA_HOME=%%~dpi.."
)

if "%JAVA_HOME%"=="" (
  echo Error: JAVA_HOME not found in your environment. >&2
  echo Please set the JAVA_HOME variable in your environment to match the >&2
  echo location of your Java installation. >&2
  exit /b 1
)

setlocal enabledelayedexpansion
set CLASSPATH=
for /R "%MAVEN_HOME%\boot" %%a in (*.jar) do set "CLASSPATH=!CLASSPATH!%%a;"
for /R "%MAVEN_HOME%\lib" %%a in (*.jar) do set "CLASSPATH=!CLASSPATH!%%a;"

"%JAVA_HOME%\bin\java.exe" ^
  -classpath "%CLASSPATH%" ^
  "-Dclassworlds.conf=%MAVEN_HOME%\bin\m2.conf" ^
  "-Dmaven.home=%MAVEN_HOME%" ^
  "-Dmaven.multiModuleProjectDirectory=%CD%" ^
  org.codehaus.plexus.classworlds.launcher.Launcher %*
