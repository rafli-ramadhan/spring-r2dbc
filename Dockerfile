# Use a base image with OpenJDK 17
FROM eclipse-temurin:17.0.8_7-jre-alpine

ENV TZ=Asia/Jakarta

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY *.jar app.jar

# Expose the port that the application will be running on
EXPOSE 8080

# Set the entry point command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
