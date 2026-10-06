# ---------- Stage 1: build the jar with Maven ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the POM first so Maven's dependency cache can be reused
# on later builds when only source files change.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Stage 2: run the jar on a slim JRE ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/creativepulse-1.0.0.jar app.jar

# Render (and most hosts) inject PORT at runtime; application.properties
# already reads it via ${PORT:8080}.
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
