# ------------ Build Stage ------------
FROM gradle:8.4.0-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test

# ------------ Run Stage ------------
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/clean-pick-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
