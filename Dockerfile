FROM eclipse-temurin:17-jre-alpine
#FROM ghcr.io/graalvm/jdk-community:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
