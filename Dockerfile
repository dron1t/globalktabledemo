FROM eclipse-temurin:17-jdk-jammy
LABEL authors="lkedr"

COPY build/libs/globalktabledemo-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "./app.jar"]