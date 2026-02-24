# Use a lightweight JRE to run the application
FROM eclipse-temurin:25-jre-alpine
LABEL authors="suvratakumarhada"
WORKDIR /app
COPY target/*.jar app.jar

# Expose HTTP and gRPC ports
EXPOSE 8080
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
