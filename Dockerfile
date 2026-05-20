# =========================
# Stage 1 - Build
# =========================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy pom first for caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests

# =========================
# Stage 2 - Runtime
# =========================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy jar from builder
COPY --from=build /app/target/*.jar app.jar

# App port
EXPOSE 8080

# JVM optimizations for containers
ENTRYPOINT ["java", "-Xms128m", "-Xmx512m", "-jar", "app.jar"]

# # Use Maven and JDK in a single stage -----------------------------------------------
# FROM maven:3.8.8-eclipse-temurin-17-alpine

# # Set the working directory
# WORKDIR /app

# # Copy pom.xml and download dependencies
# COPY pom.xml /app/pom.xml
# RUN mvn dependency:go-offline

# # Copy the entire source code and build the WAR/JAR file
# COPY . /app
# RUN mvn package -DskipTests

# # Verify the JAR file exists in the target directory (optional, for debugging)
# RUN ls -al /app/target

# # Expose the port the application will run on
# EXPOSE 8080

# # Specify the command to run the application (assuming JAR is created in /target)
# CMD ["java", "-jar", "/app/target/your-app.jar"]
