# Database

The application uses PostgreSQL with Hibernate validation mode. Runtime table generation is disabled, so the schema must exist before startup.

## Initialization

```bash
createdb badran_store
psql -d badran_store -f sql/init.sql
```

## ER Diagram

```mermaid
erDiagram
    ROLES ||--o{ USERS : assigns
    USERS ||--o{ ADDRESSES : owns
    CATEGORIES ||--o{ CATEGORIES : parent
    CATEGORIES ||--o{ PRODUCTS : classifies
    BRANDS ||--o{ PRODUCTS : brands
    PRODUCTS ||--o{ PRODUCT_IMAGES : has
    PRODUCTS ||--o{ WISHLIST : saved
    PRODUCTS ||--o{ REVIEWS : reviewed
    CART ||--o{ CART_ITEMS : contains
    COUPONS ||--o{ ORDERS : discounts
    ORDERS ||--o{ ORDER_ITEMS : contains
    ORDERS ||--o{ PAYMENTS : paid_by
```

## Important Tables

- `roles`, `users`, `addresses`
- `brands`, `categories`, `products`, `product_images`
- `cart`, `cart_items`
- `coupons`, `orders`, `order_items`, `payments`
- `reviews`, `wishlist`

## JSONB Columns

- `roles.permissions`
- `products.specifications`

## Validation Mode

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```
