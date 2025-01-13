FROM gradle:jdk17 as builder

WORKDIR /build

COPY . /build

RUN gradle build --exclude-task test

FROM openjdk:17-slim

WORKDIR /app

COPY --from=builder /build/build/libs/devinit-back-*.jar app.jar

EXPOSE 8080

USER nobody

ENTRYPOINT ["java", "-jar", "app.jar"]