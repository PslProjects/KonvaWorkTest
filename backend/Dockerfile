FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", \
  "--spring.config.location=/app/config/application.properties", \
  "--server.port=9999"]