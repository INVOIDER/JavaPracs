FROM maven:3.8.6-ibm-semeru-17-focal
RUN mvn --version

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY src /app/src

RUN mvn clean install -DskipTests

FROM openjdk:17-oracle

LABEL name="ZEA"

ARG JAR_FILE=/app/target/*.jar
COPY --from=builder ${JAR_FILE} /application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
