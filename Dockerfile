FROM eclipse-temurin:17-jre-alpine
# FROM ghcr.io/graalvm/jdk-community:17

WORKDIR /glowroot

RUN apk update && apk add --update curl && apk add unzip \
    && curl -L -o glowroot.zip https://github.com/glowroot/glowroot/releases/download/v0.14.0/glowroot-0.14.0-dist.zip \
    && unzip glowroot.zip -d / \
    && rm glowroot.zip \
    && echo '{ "web": { "bindAddress": "0.0.0.0" } }' > /glowroot/admin.json

# RUN mkdir -p /glowroot /glowroot/tmp /glowroot/logs /glowroot/plugins && \
#    echo '{ "web": { "bindAddress": "0.0.0.0" } }' > /glowroot/admin.json

# ADD target/glowroot.jar /glowroot

ARG JAR_FILE=target/rinha*.jar

COPY ${JAR_FILE} app.jar

ENV JAVA_OPTS ${JAVA_OPTS}

ENTRYPOINT exec java ${JAVA_OPTS} -jar app.jar