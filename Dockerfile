FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine

COPY --from=build target/javazilla-0.0.1-SNAPSHOT.jar javazilla.jar
ENTRYPOINT ["java","-jar","/javazilla.jar"]