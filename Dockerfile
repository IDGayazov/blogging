FROM openjdk:17-slim

WORKDIR /app

RUN apt-get update -y && \
apt-get install findutils -y && \
apt-get clean

COPY . .

RUN mvn build -x test -Pversion=0.0.1-SNAPSHOT

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "build/libs/app-0.0.1-SNAPSHOT.jar"]