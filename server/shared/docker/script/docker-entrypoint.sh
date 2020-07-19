#!/bin/bash -e
##-------------------------------------------------------------------
## @copyright 2018 Coinsthai.com
## All Rights Reserved.
##
## File : docker-entrypoint.sh
## Author : developer@coinsthai.com
## Description :
##-------------------------------------------------------------------

JAR_VERSION=1.0.0

echo "Using JAVA_OPTS=$JAVA_OPTS"
echo "CMD Args: $@"

bash /docker-wait.sh "nc -z -v -w 5 db 3306" 60 &&\
java $JAVA_OPTS -jar "/app.jar" "$@"
## File : docker-entrypoint.sh ends