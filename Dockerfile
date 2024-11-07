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
RUN apk add --no-cache curl && \
    curl -o app.jar "$NEXUS_URL/repository/$NEXUS_REPO/$(echo $GROUP_ID | tr . /)/$ARTIFACT_ID/$VERSION/$ARTIFACT_ID-$VERSION.jar"
# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
