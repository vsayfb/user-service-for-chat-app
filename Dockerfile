FROM eclipse-temurin:17-alpine as base

WORKDIR /app

COPY pom.xml .
COPY src src
COPY mvnw .
COPY .mvn .mvn

RUN --mount=type=cache,target=/root/.m2 \ 
    ./mvnw dependency:resolve

ENTRYPOINT ["./mvnw", "spring-boot:run"]
