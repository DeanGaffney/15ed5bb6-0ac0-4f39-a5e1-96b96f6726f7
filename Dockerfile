FROM eclipse-temurin:11-jdk-alpine 

ENV APP_HOME=/usr/app
ENV ARTIFACT_NAME=sensor-0.0.1-SNAPSHOT.jar

WORKDIR $APP_HOME

COPY build.gradle ${APP_HOME}
COPY settings.gradle ${APP_HOME}
COPY gradlew ${APP_HOME}

COPY gradle ${APP_HOME}/gradle

COPY ./src ${APP_HOME}/src

RUN ./gradlew build -x test -x integrationTest || return 1

EXPOSE 8080

ENTRYPOINT java -jar ${APP_HOME}/build/libs/$ARTIFACT_NAME
