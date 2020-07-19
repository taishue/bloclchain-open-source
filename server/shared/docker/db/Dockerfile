######################### How To Use Docker Image #############################################
##
##  Image Name: coinsthai/db
##  How To Build Docker Image: go to project root directory then run
##  docker build --no-cache -f shared/docker/db/Dockerfile -t coinsthai/db --rm=true .
##  docker build -f shared/docker/db/Dockerfile -t coinsthai/db .
##  Docker hub link:
##  Description: This docker container is meant to hold a relational database instance
##      that can be shared across multiple persistence needy services.
##
###############################################################################################
# Base Docker image: https://hub.docker.com/_/mysql/
FROM mysql:5.7.21

LABEL maintainer "developer@coinsthai.com"


ENV DB_PORT 3306
ENV MYSQL_VERSION 14.14

RUN apt-get -qq update && \
    # Install facility tools
    apt-get install -y --no-install-recommends lsof bash && \
    # Verify docker images
    which lsof && which bash && \
    mysql --version | grep $MYSQL_VERSION

# docker healthcheck: mysql query
HEALTHCHECK --interval=2m --timeout=3s \
            CMD mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -P$DB_PORT $MYSQL_DATABASE -e "select count(1) from t_coin;" || exit 1

EXPOSE $DB_PORT
