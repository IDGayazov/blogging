# Используем базовый образ с Java 17
FROM maven:3.8.4-openjdk-17-slim AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Устанавливаем необходимые пакеты (если они действительно нужны)
RUN apt-get update -y && \
    apt-get install -y findutils && \
    apt-get clean

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Копируем только нужные файлы (используйте .dockerignore для исключения ненужных файлов)
COPY . .

# Собираем проект с помощью Maven
RUN mvn clean package -DskipTests

# Указываем порт, который будет использоваться приложением
EXPOSE 8082

# Запускаем приложение
# ENTRYPOINT ["java", "-jar", "target/project-0.0.1-SNAPSHOT.jar"]
ENTRYPOINT ["/entrypoint.sh"]