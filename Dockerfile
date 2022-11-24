# Build stage
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package -DskipTests

# Run stage
FROM openjdk:11-jre-slim
COPY --from=build /app/target/*.jar /app/app.jar
COPY src/main/resources/application-prod.properties /app/application.properties
EXPOSE 8081
ENTRYPOINT java -jar /app/app.jar --spring.config.location=/app/application.properties
