#!/bin/bash
runJavaHome() {
	$JAVA_HOME/bin/java -jar jchecksum-1.0.1904.jar $@
}
runDefaultJava() {
	java -jar jchecksum-1.0.1904.jar $@
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

