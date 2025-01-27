FROM gradle:jdk17 as builder

WORKDIR /build

COPY . /build

RUN echo "Current directory1:" && pwd

RUN gradle build --exclude-task test

FROM openjdk:17-slim

WORKDIR /app
RUN echo "Current directory2:" && pwd
COPY --from=builder /build/build/libs/devinit-back-*.jar app.jar

EXPOSE 8080

USER nobody

ENTRYPOINT ["java", "-jar", "app.jar"]
