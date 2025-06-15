FROM maven:3.9.5-eclipse-temurin-17 as build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
COPY --from-build /app/target/store-2.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
