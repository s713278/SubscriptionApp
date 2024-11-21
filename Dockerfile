# Use a base image with Java runtime
FROM openjdk:21-jdk

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container with the desired name
COPY target/SubNRenewals-1.0.jar SubNRenewals.jar

# Copy the profile-specific properties file
COPY src/main/resources/application-docker.properties /app/application-docker.properties
COPY src/main/resources/application.yml /app/application.yml

# Specify the active profile
ENV SPRING_PROFILES_ACTIVE=docker

# Run the JAR file
ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar", "SubNRenewals.jar"]
