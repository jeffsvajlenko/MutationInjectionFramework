@ECHO OFF



REM Determines which directory contains the needed jar files
set JAR_DIR=jar



REM Adds all jar files to the classpath

set CP=%JAR_DIR%\ccsm-commons.jar;%JAR_DIR%\iclones.jar;%JAR_DIR%\diff-utils-1.2.jar;%JAR_DIR%\rcf.jar;%JAR_DIR%\scanner.jar;%JAR_DIR%\swt.jar



REM Now start iclones using the assembled classpath

java -Xmx2048m -cp %CP% de.uni_bremen.st.iclones.IClones %*

