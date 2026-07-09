# Badran Car Wash Supplies & Car Accessories Store Backend

Spring Boot modular monolith backend for a university car wash supplies and accessories store. The application exposes REST APIs for authentication, product catalog browsing, wishlist, cart, checkout, orders, payments, and reviews.

## Architecture

The project is a single Spring Boot application organized by business modules under `com.badran.store`. Controllers call application services directly, services coordinate repositories and mappers, and JPA entities preserve the existing PostgreSQL schema.

Core modules:

- `auth`: registration, login, profile, password reset DTOs and controller.
- `security`: JWT utilities, authentication filter, security configuration.
- `product`: catalog search, product details, stock deduction.
- `cart`: cart controller, DTOs, entities, repositories.
- `order`: checkout, order lookup, payment completion.
- `wishlist`: saved product entries.
- `review`: product reviews.
- `brand`, `category`, `coupon`, `user`: supporting catalog, discount, and account data.
- `common`, `exception`, `config`: shared responses, exception handling, OpenAPI configuration.

See [docs/Architecture.md](docs/Architecture.md) for Mermaid architecture, package, and request-flow diagrams.

## Technologies

- Java 21
- Spring Boot 3.4.7
- Spring Security with JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Bean Validation
- MapStruct
- Lombok
- Springdoc OpenAPI / Swagger UI
- Maven
- Docker and Docker Compose
- GitHub Actions

## Folder Structure

```text
src/main/java/com/badran/store
  auth/         authentication API
  brand/        product brand data
  cart/         shopping cart API and persistence
  category/     catalog category data
  common/       shared constants and response envelopes
  config/       OpenAPI configuration
  coupon/       checkout coupon data
  exception/    global API exception handling
  order/        checkout, order, and payment workflows
  product/      product catalog, stock, wishlist/review bridge
  review/       review data
  security/     JWT and Spring Security
  user/         user, role, and address data
  wishlist/     wishlist data
docs/           developer documentation and generated API artifacts
postman/        Postman collection
sql/            PostgreSQL initialization script
.github/        CI workflow
```

## Installation

Requirements:

- Java 21
- Maven 3.9+
- PostgreSQL 17 or compatible PostgreSQL server
- Docker and Docker Compose, optional

Clone the repository, create the database, load the SQL initialization script, then build:

```bash
createdb badran_store
psql -d badran_store -f sql/init.sql
mvn clean install
```

## Environment Variables

Profiles are split by environment:

- `dev`: local PostgreSQL defaults, no SQL logging by default.
- `test`: used by automated tests. Defaults to the same local PostgreSQL database unless `TEST_DB_*` variables are provided.
- `prod`: requires database credentials and `JWT_SECRET` from environment variables.

| Variable | Description |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `dev`, `test`, or `prod`. Defaults to `dev`. |
| `SERVER_PORT` | HTTP port. Defaults to `8080`. |
| `DB_URL` | JDBC URL, for example `jdbc:postgresql://localhost:5432/badran_store`. |
| `DB_USERNAME` | PostgreSQL username. |
| `DB_PASSWORD` | PostgreSQL password. |
| `JWT_SECRET` | JWT signing secret. Must be at least 32 bytes. |
| `JWT_EXPIRATION_MS` | JWT lifetime in milliseconds. Defaults to one day. |
| `TEST_DB_URL` | Optional JDBC URL for integration tests. |
| `TEST_DB_USERNAME` | Optional PostgreSQL username for integration tests. |
| `TEST_DB_PASSWORD` | Optional PostgreSQL password for integration tests. |

Use [.env.example](.env.example) as a template for local Docker or shell configuration.

## Database

The application uses `spring.jpa.hibernate.ddl-auto=validate`; it validates the schema and does not create or migrate tables at runtime.

Database documentation:

- [docs/Database.md](docs/Database.md)
- [sql/init.sql](sql/init.sql)

## Swagger

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Static OpenAPI artifact: [docs/openapi.json](docs/openapi.json)

Use `/api/v1/auth/login` to obtain a JWT, then authorize Swagger with:

```text
Bearer <token>
```

## Running Locally

```bash
mvn clean install
mvn spring-boot:run
```

The default dev profile expects:

```text
jdbc:postgresql://localhost:5432/badran_store
username: najwanbadran
password: empty
```

Override with environment variables when needed:

```bash
SPRING_PROFILES_ACTIVE=dev \
DB_URL=jdbc:postgresql://localhost:5432/badran_store \
DB_USERNAME=my_user \
DB_PASSWORD=my_password \
JWT_SECRET=my-secure-secret-with-at-least-32-bytes \
mvn spring-boot:run
```

## Docker

Set a strong JWT secret first:

```bash
export JWT_SECRET=my-secure-secret-with-at-least-32-bytes
docker compose up --build
```

The app is available at `http://localhost:8080`.

## Testing

Run all tests:

```bash
mvn clean install
```

The current suite includes JWT utility tests, auth controller tests, service tests, and Spring Boot MockMvc integration smoke tests.

## API Tools

- Postman collection: [postman/badran-store-api.postman_collection.json](postman/badran-store-api.postman_collection.json)
- API documentation: [docs/API.md](docs/API.md)
- Mermaid diagrams: [docs/mermaid](docs/mermaid)

## Deployment

See [docs/Deployment.md](docs/Deployment.md) for profile usage, Docker, CI, and production environment guidance.
