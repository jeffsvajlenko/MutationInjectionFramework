#!/bin/bash

# Directory where the script (and the jar directory) is located
SCRIPT_DIR=$(dirname $0)

# Determines which directory contains the needed jar files
JAR_DIR=${SCRIPT_DIR}/jar

# Adds all jar files to the classpath
CP=\
${JAR_DIR}/ccsm-commons.jar:\
${JAR_DIR}/iclones.jar:\
${JAR_DIR}/diff-utils-1.2.jar:\
${JAR_DIR}/rcf.jar:\
${JAR_DIR}/scanner.jar

# Options passed to the Java VM
OPTIONS=-Xmx8000m

# Now start iclones using the assembled classpath. 
java ${OPTIONS} -cp ${CP} de.uni_bremen.st.iclones.IClones $*
