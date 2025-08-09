FROM openjdk:23-jdk
COPY target/*.jar app.jar
LABEL authors="Victus"

ENTRYPOINT ["java", "-jar", "/app.jar"]