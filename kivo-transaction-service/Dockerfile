FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .

COPY kivo-user-service/pom.xml kivo-user-service/pom.xml
COPY kivo-transaction-service/pom.xml kivo-transaction-service/pom.xml
COPY kivo-analytics-service/pom.xml kivo-analytics-service/pom.xml

COPY kivo-transaction-service/src kivo-transaction-service/src

RUN mvn -q -pl kivo-transaction-service -am -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/kivo-transaction-service/target/*.jar app.jar
ENTRYPOINT ["sh","-c","java -jar app.jar"]
