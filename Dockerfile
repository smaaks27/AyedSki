# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

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
RUN apt-get update && apt-get install -y curl && \
    curl -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} \
    -o app.jar "${NEXUS_URL}/repository/${NEXUS_REPO}/${NEXUS_GROUP_ID//.//}/${NEXUS_ARTIFACT_ID}/${NEXUS_VERSION}/${NEXUS_ARTIFACT_ID}-${NEXUS_VERSION}.jar"

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
