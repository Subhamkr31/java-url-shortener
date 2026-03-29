FROM maven:3.9.11-eclipse-temurin-24-alpine AS build

WORKDIR /usr/app

COPY pom.xml ./
COPY src ./src

RUN mvn install
RUN mvn package

FROM eclipse-temurin:24-jre-alpine AS production

WORKDIR /app

COPY --from=build /usr/app/target/*.jar app.jar
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]