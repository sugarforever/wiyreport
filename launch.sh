#/usr/bin/sh

export JAVA_OPTS="-Xmx768m"
java -Xmx768m -jar wiyreport-0.0.2.jar --spring.config.location=$1
