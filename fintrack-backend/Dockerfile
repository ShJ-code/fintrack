# ---- Stage 1: build the JAR ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy only pom first so dependencies cache independently of source changes.
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Now copy source and build.
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Stage 2: runtime image ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the JAR from the build stage.
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]