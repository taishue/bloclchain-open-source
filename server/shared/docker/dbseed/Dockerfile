######################################## How To Use Docker Image #############################################
##
##  Image Name: coinsthai/dbseed
##  How To Build Docker Image: go to project root directory then run
##  docker build --no-cache -f shared/docker/dbseed/Dockerfile -t coinsthai/dbseed --rm=true .
##  docker build -f shared/docker/dbseed/Dockerfile -t coinsthai/dbseed .
##  Docker hub link:
##  Description: This docker container is meant to seed the relational database with initial
##      data that will help prepare the system for immediate usability.
##
##############################################################################################################
FROM coinsthai/jdk

LABEL maintainer "developer@coinsthai.com"

# TODO: confirm whether we need root
# USER root

ENV LB_VERSION=3.1.1
ENV LB_NAME liquibase-$LB_VERSION-bin
ENV LB_HOME /opt/liquibase

COPY shared/docker/dbseed/$LB_NAME.zip /

RUN echo Using Liquibase version $LB_VERSION && \
  mkdir -p $LB_HOME && \
  unzip $LB_NAME.zip -d $LB_HOME/ && \
  rm -f $LB_NAME.zip && \
  chmod +x $LB_HOME/liquibase && \
  ln -s $LB_HOME/liquibase /usr/local/bin/

ENV MYSQL_CONN_VERSION 5.1.41
ENV MYSQL_NAME mysql-connector-java-$MYSQL_CONN_VERSION
ENV DB_DRIVER_HOME /opt/db_drivers

COPY shared/docker/dbseed/$MYSQL_NAME.zip /

RUN echo Using MySQL Connector-J version $MYSQL_CONN_VERSION && \
  # TODO: replace with ADD instruction
  mkdir -p $DB_DRIVER_HOME && \
  unzip $MYSQL_NAME.zip -d $DB_DRIVER_HOME/ && \
  rm -f $MYSQL_NAME.zip && \
  chmod +x $DB_DRIVER_HOME/$MYSQL_NAME/$MYSQL_NAME-bin.jar && \
  mkdir -p $LB_HOME/lib && \
  ln -s $DB_DRIVER_HOME/$MYSQL_NAME/$MYSQL_NAME-bin.jar $LB_HOME/lib

ADD shared/docker/dbseed/changelogs.xml /

ENV DB_PORT 3306
ENV DB_DRIVER com.mysql.jdbc.Driver
ENV CHANGE_LOG_FILE /changelogs.xml

# TODO: Enable healthcheck, and merge dbseed with db

ADD shared/docker/script/docker-wait.sh /

ENTRYPOINT \
    bash /docker-wait.sh "nc -z -v -w 5 db ${DB_PORT}" 300 && \
    liquibase --changeLogFile=$CHANGE_LOG_FILE \
        --driver="${DB_DRIVER}" --url="${DB_URL}" --username="root" --password="${DB_ROOT_PASSWORD}" \
        update -DHOST="${HOST}" -DHOST_SCOPE="${HOST_SCOPE}" -DIOT_HOST="${IOT_HOST}" -DMES_HOST="${MES_HOST}" "$@"
