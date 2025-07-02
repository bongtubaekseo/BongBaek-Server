FROM openjdk:21-slim
WORKDIR /app
COPY build/libs/*.jar ./bongbaek.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/bongbaek.jar"]