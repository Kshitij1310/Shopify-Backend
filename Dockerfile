FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw -q -DskipTests dependency:go-offline
COPY src src
RUN ./mvnw -q -DskipTests package
EXPOSE 8081
ENTRYPOINT ["java","-jar","target/ecommerce-springboot-0.0.1-SNAPSHOT.jar"]
