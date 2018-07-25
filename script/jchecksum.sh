#!/bin/bash

runJavaHome() {
	CLASS_PATH=./jchecksum-0.0.1-jar-with-dependencies.jar
	$JAVA_HOME/bin/java -cp $CLASS_PATH io.apercova.jchecksum.CheckSum $@
}
runDefaultJava() {
	CLASS_PATH=./jchecksum-0.0.1-jar-with-dependencies.jar
	java -cp $CLASS_PATH io.apercova.jchecksum.CheckSum $@
}

if [ "$JAVA_HOME" == "" ]; then
	if [ "$(command -v java)" != "" ]; then
		runDefaultJava "$@"
	else
		echo "Unable to find java"
		echo -n "set up JAVA_HOME: "
		read resp
		if [ "$resp" != "" ]; then
			JAVA_HOME=$resp
			runJavaHome "$@"
		else
			echo "java is required"
			exit 1
		fi
	fi
else
	runJavaHome "$@"
fi
