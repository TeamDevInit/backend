# 1단계: 빌드
FROM gradle:jdk17 as builder

WORKDIR /build

# Gradle Wrapper와 프로젝트 소스를 복사
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY .gradle .gradle
COPY src ./src

# Gradle 빌드 (테스트 제외)
RUN ./gradlew clean build -x test

# 2단계: 실행 환경 생성
FROM openjdk:17-slim

WORKDIR /app

# 빌드 아티팩트를 복사
COPY --from=builder /build/build/libs/devinit-back-*.jar app.jar

EXPOSE 8080

USER nobody

ENTRYPOINT ["java", "-jar", "app.jar"]