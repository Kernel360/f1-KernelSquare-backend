FROM openjdk:21-jdk

ARG JAR_FILE
ARG PROFILES
ARG ENV

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "/app.jar"]
