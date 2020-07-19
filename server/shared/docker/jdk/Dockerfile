######################################## How To Use Docker Image #############################################
##
##  Image Name: coinsthai/jdk
##  Docker hub link:
##  How To Build Docker Image: go to project root directory then run
##  docker build --no-cache -f shared/docker/jdk/Dockerfile -t coinsthai/jdk .
##  docker build -f shared/docker/jdk/Dockerfile -t coinsthai/jdk .
##  Description: Base image for all java repos with jdk installed
##
##############################################################################################################
# Base Docker image: https://hub.docker.com/_/openjdk/

FROM openjdk:8-alpine

RUN apk add --update curl bash lsof tree && \
    # Clean up
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /var/cache/apk/*

# Install basic common scripts
ADD shared/docker/script/docker-wait.sh /

RUN chmod + /*.sh && \
    # Verify docker image
    java -version 2>&1 | grep "1.8.0" && \
    # curl --version | grep "7.56.0" && \
    # lsof -v 2>&1 | grep "4.89" && \
    # bash --version | grep "4.3.48" && \
    # tree --version | grep "1.7.0"
    which curl && which bash && which lsof && which tree
