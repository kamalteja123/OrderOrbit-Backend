# Use the official OpenJDK image as a base
FROM openjdk:17-jdk-alpine
 
# Set the working directory inside the container
WORKDIR /app
 
# Copy the application JAR file to the container (using a wildcard)
COPY target/*.jar app.jar
 
# Expose the port the application runs on
EXPOSE 8081
 
# Run the JAR file
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${SERVER_PORT}"]
