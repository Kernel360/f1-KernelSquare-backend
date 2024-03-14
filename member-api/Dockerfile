FROM openjdk:21-jdk

ARG PROFILES
ARG ENV

COPY build/libs/member-api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]
