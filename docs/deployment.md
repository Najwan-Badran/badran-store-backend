# Deployment

## Build

```bash
mvn clean install
```

## Run Locally

```bash
mvn spring-boot:run
```

## Required Production Environment Variables

```text
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
DB_URL=jdbc:postgresql://<host>:5432/badran_store
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>
JWT_SECRET=<at-least-32-bytes>
JWT_EXPIRATION_MS=86400000
```

## Docker

Dockerfile and Docker Compose are present in the repository.

```bash
export JWT_SECRET=my-secure-secret-with-at-least-32-bytes
docker compose up --build
```

## CI

GitHub Actions workflow:

```text
.github/workflows/build.yml
```

The workflow builds, tests, initializes PostgreSQL, and packages the Spring Boot artifact.

## Operational Checks

```text
GET /actuator/health
GET /v3/api-docs
GET /swagger-ui.html
```
