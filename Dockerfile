# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-alpine
# Set environment variables for Nexus access
ARG NEXUS_URL
ARG NEXUS_REPO
ARG NEXUS_GROUP_ID
ARG NEXUS_ARTIFACT_ID
ARG NEXUS_VERSION
ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD

# Set working directory
WORKDIR /app

# Download the JAR file from Nexus
RUN apk add --no-cache curl && \
    curl -o app.jar http://192.168.50.4:8081/repository/AyedSki/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar
# Expose the port your app runs on
EXPOSE 8089

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
