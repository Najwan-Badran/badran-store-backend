# Architecture

Badran Store is a Spring Boot modular monolith. It runs as one deployable application while keeping business capabilities separated into packages under `com.badran.store`.

## Runtime View

```mermaid
flowchart TB
    Client[Client / Swagger / Postman] --> Security[Spring Security JWT Filter]
    Security --> Controllers[REST Controllers]
    Controllers --> Services[Application Services]
    Services --> Repositories[Spring Data JPA Repositories]
    Repositories --> PostgreSQL[(PostgreSQL)]
    Services --> MapStruct[MapStruct Mappers]
    MapStruct --> DTOs[API DTOs]
```

## Module View

```mermaid
flowchart LR
    Root[com.badran.store]
    Root --> auth
    Root --> security
    Root --> user
    Root --> product
    Root --> cart
    Root --> order
    Root --> wishlist
    Root --> review
    Root --> brand
    Root --> category
    Root --> coupon
    Root --> common
    Root --> exception
    Root --> config

    auth --> user
    auth --> security
    order --> cart
    order --> product
    order --> coupon
    product --> brand
    product --> category
    product --> wishlist
    product --> review
```

## Request Flow

```mermaid
sequenceDiagram
    participant Client
    participant Filter as JWT Filter
    participant Controller
    participant Service
    participant Repository
    participant DB as PostgreSQL

    Client->>Filter: HTTP request
    Filter->>Filter: Validate bearer JWT
    Filter->>Controller: Authenticated request
    Controller->>Service: Invoke business use case
    Service->>Repository: Load or persist entities
    Repository->>DB: SQL through Hibernate
    DB-->>Repository: Query result
    Repository-->>Service: Entity result
    Service-->>Controller: DTO result
    Controller-->>Client: ApiResponse JSON
```

Standalone Mermaid files are available under `docs/mermaid/`.
