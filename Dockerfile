# 이미지 빌드
FROM gradle:8.14.2-jdk21 AS builder
WORKDIR /app
# Gradle 의존성 캐싱 최적화를 위해 build script 파일들만 먼저 복사
COPY build.gradle settings.gradle gradle.properties gradle /app/
# 의존성 미리 다운로드
RUN gradle dependencies --no-daemon || true
COPY . /app
RUN ./gradlew clean build -x test --no-daemon

# 컨테이너 실행
FROM openjdk:21-slim
COPY --from=builder /app/build/libs/*.jar /bongbaek.jar
EXPOSE 8080
CMD ["java", "-jar", "/bongbaek.jar"]