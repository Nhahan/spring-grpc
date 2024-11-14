# Stage 1: Build
FROM gradle:8.5.0-jdk17-focal AS build

# Set the working directory
WORKDIR /home/gradle/project

# Copy the entire project
COPY . .

# Build the client JAR using Gradle
RUN gradle :client:clean build --no-daemon

# Stage 2: Run
FROM amazoncorretto:17-alpine AS runtime

# Set the working directory
WORKDIR /app

# Copy the built client JAR from the build stage
COPY --from=build /home/gradle/project/client/build/libs/*.jar client.jar

# Expose the REST API port
EXPOSE 8080

# Run the client
ENTRYPOINT ["java", "-jar", "client.jar"]
