FROM openjdk:21-slim
RUN apt-get update && apt-get install -y curl ca-certificates && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY build/libs/*.jar ./bongbaek.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/bongbaek.jar"]
