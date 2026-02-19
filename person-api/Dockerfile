# Image Java officielle
FROM eclipse-temurin:21-jdk-alpine

# Dossier de travail dans le conteneur
WORKDIR /app

# Copier le JAR généré par Maven
COPY target/personapi-0.0.1-SNAPSHOT.jar app.jar


# Port exposé par l'API
EXPOSE 9000

# Lancement de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
