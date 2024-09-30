ARG MS_NAME=kitchensink

FROM openjdk:21-jdk-slim AS builder

ARG MS_NAME

LABEL authors="Yvancho"

WORKDIR /app/$MS_NAME

COPY ./pom.xml /app
COPY ./.mvn ./.mvn
COPY ./mvnw .
COPY ./pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target

COPY ./src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk-slim

ARG MS_NAME

WORKDIR /app
RUN mkdir ./logs
ARG TARGET_FOLDER=/app/$MS_NAME/target
COPY --from=builder $TARGET_FOLDER/kitchensink-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=9090
ENV PORT=$PORT_APP
EXPOSE $PORT

CMD ["java", "-jar", "kitchensink-0.0.1-SNAPSHOT.jar"]

# docker build -t kitchensink . -f .\Dockerfile
# docker tag kitchensink yvancho/kitchensink:latest
# docker push yvancho/kitchensink:latest
