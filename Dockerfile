# ETAPA 1: Compilación (Actualizado a Java 21)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Aprovechamos el cache para las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (Actualizado a JRE 21)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]