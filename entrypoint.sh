#!/bin/sh
export SPRING_DATASOURCE_USERNAME=$(cat /run/secrets/spring_datasource_username)
export SPRING_DATASOURCE_PASSWORD=$(cat /run/secrets/spring_datasource_password)
export TOKEN_SIGNING_KEY=$(cat /run/secrets/token_signing_key)

exec java -jar /app/target/project-0.0.1-SNAPSHOT.jar