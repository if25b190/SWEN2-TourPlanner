# syntax=docker/dockerfile:1.7

# BUILD FRONTEND
FROM node:24.14.1-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN --mount=type=cache,target=/root/.npm npm ci
COPY frontend/ ./
RUN mkdir -p /app/backend/src/main/resources && npm run build

# BUILD BACKEND
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app/backend
COPY backend/ ./
COPY --from=frontend-build /app/backend/src/main/resources/static ./src/main/resources/static
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

# RUN
FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=build /app/backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
