#
# Build stage
#
FROM maven:3.8.7-openjdk-18-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
#
# Package stage
#
FROM openjdk:18-jdk-slim
COPY --from=build /app/target/*.jar zine.jar
COPY src/main/resources/zine-firebase-admin.json /app/zine-firebase-admin.json

# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","zine.jar"]
