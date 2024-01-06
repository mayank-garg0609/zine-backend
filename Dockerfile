#
# Build stage
#
FROM maven:3.8.7-openjdk-18-slim AS build
COPY . .
RUN mvn clean package

#
# Package stage
#
FROM openjdk:18-jdk-slim
COPY --from=build /target/zine-0.0.1-SNAPSHOT.jar zine.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","zine.jar"]