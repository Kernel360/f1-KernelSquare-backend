FROM openjdk:21-jdk

COPY build/libs/alert-api-0.0.1-SNAPSHOT.jar alert-api/alert-app.jar

WORKDIR /alert-api
ENTRYPOINT ["java", "-jar", "alert-app.jar"]
