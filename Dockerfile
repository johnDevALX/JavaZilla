FROM openjdk:17-alpine

COPY target/javazilla-0.0.1-SNAPSHOT.jar javazilla.jar
ENTRYPOINT ["java","-jar","/javazilla.jar"]