# Блог-платформа (REST API)

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![Swagger](https://img.shields.io/badge/Swagger-3.0-green)](https://swagger.io/)

REST API для платформы блогов с авторизацией, комментариями и лайками.  
Проект развертывается в Docker с защитой данных через Docker Secrets.

## 📋 Функционал
- ✅ Регистрация/авторизация (JWT)
- ✅ CRUD для статей, комментариев, категорий
- ✅ Лайки и тегирование
- ✅ Загрузка изображений
- 📊 Swagger-документация
- � 80% покрытие тестами

## 🚀 Запуск проекта

### Требования
- Docker 20.10+
- Docker Compose 2.0+

### 1. Настройка секретов
Создайте секреты для БД:
```bash
echo "your_db_user" | docker secret create db_username -
echo "your_db_password" | docker secret create db_password -
```

### 2. Запуск контейнеров
```bash
docker-compose up --build
```

### 3. Доступ к сервисам

API: http://localhost:8082

Swagger UI: http://localhost:8082/swagger-ui.html
