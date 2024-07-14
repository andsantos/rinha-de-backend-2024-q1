# FROM eclipse-temurin:17-jre-alpine
FROM ghcr.io/graalvm/jdk-community:17

#RUN mkdir -p /glowroot /glowroot/tmp /glowroot/logs /glowroot/plugins && \
#    echo '{ "web": { "bindAddress": "0.0.0.0" } }' > /glowroot/admin.json

RUN mkdir -p /glowroot /glowroot/tmp /glowroot/logs /glowroot/plugins

ADD target/glowroot.jar /glowroot
ADD docker/glowroot.properties /glowroot
#ADD target/glowroot-plugins /glowroot/plugins

ARG JAR_FILE=target/rinha*.jar

COPY ${JAR_FILE} app.jar

ENV JAVA_OPTS ${JAVA_OPTS}

# ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT exec java ${JAVA_OPTS} -jar app.jar