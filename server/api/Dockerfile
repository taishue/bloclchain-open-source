######################################## How To Use Docker Image #############################################
##
##  Image Name: coinsthai/api
##  How To Build Docker Image: go to project root directory then run
##  docker build --no-cache -f api/Dockerfile -t coinsthai/api --rm=true .
##  docker build -f api/Dockerfile -t coinsthai/api .
##  Docker hub link:
##  Description: This docker container is meant to seed the relational database with initial
##      data that will help prepare the system for immediate usability.
##
##############################################################################################################

FROM coinsthai/jdk

LABEL maintainer "developer@coinsthai.com"

ADD api/build/libs/api.jar /app.jar
ADD shared/docker/script /

RUN chmod +x /*.sh

EXPOSE 8080

WORKDIR /

ENTRYPOINT /docker-entrypoint.sh

HEALTHCHECK --interval=2m --timeout=3s \
            CMD bash -xe /docker-healthcheck.sh || exit 1
