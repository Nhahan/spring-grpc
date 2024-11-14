# Stage 1: Build
FROM gradle:8.5.0-jdk17-focal AS build

# Set the working directory
WORKDIR /home/gradle/project

# Copy the entire project
COPY . .

# Build the server JAR using Gradle
RUN gradle :server:clean build --no-daemon

# Stage 2: Run
FROM amazoncorretto:17-alpine AS runtime

# Set the working directory
WORKDIR /app

# Copy the built server JAR from the build stage
COPY --from=build /home/gradle/project/server/build/libs/*.jar server.jar

# Expose the gRPC port
EXPOSE 9090 8081

# Run the server
ENTRYPOINT ["java", "-jar", "server.jar"]
