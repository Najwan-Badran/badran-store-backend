# API

## Base URL

```text
http://localhost:8080
```

## Authentication

Public endpoints:

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /actuator/health`
- `GET /v3/api-docs`
- `GET /swagger-ui.html`

Protected endpoints require:

```text
Authorization: Bearer <jwt>
```

User-scoped endpoints also use:

```text
X-User-Id: <authenticated-user-id>
```

## Endpoint Summary

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Register a customer. |
| `POST` | `/api/v1/auth/login` | Authenticate and return JWT. |
| `GET` | `/api/v1/auth/me` | Retrieve authenticated profile. |
| `POST` | `/api/v1/auth/forgot-password` | Generate password reset token. |
| `POST` | `/api/v1/auth/reset-password` | Reset password by token. |
| `GET` | `/api/v1/products` | Search products. |
| `GET` | `/api/v1/products/{id}` | Get product details. |
| `PUT` | `/api/v1/products/internal/{id}/deduct-stock` | Deduct stock. |
| `GET` | `/api/v1/cart` | Get cart. |
| `POST` | `/api/v1/cart/items` | Add item to cart. |
| `DELETE` | `/api/v1/cart/items/{productId}` | Remove item from cart. |
| `DELETE` | `/api/v1/cart` | Clear cart. |
| `POST` | `/api/v1/orders` | Create order from cart. |
| `GET` | `/api/v1/orders/{orderId}` | Get order. |
| `GET` | `/api/v1/orders` | List user orders. |
| `POST` | `/api/v1/orders/{orderId}/payment` | Complete order payment. |
| `GET` | `/api/v1/wishlist` | List wishlist. |
| `POST` | `/api/v1/wishlist/{productId}` | Add wishlist item. |
| `DELETE` | `/api/v1/wishlist/{productId}` | Remove wishlist item. |
| `GET` | `/api/v1/reviews/product/{productId}` | List product reviews. |
| `POST` | `/api/v1/reviews/product/{productId}` | Add product review. |

## Tools

- Runtime Swagger UI: `http://localhost:8080/swagger-ui.html`
- Runtime OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Static OpenAPI JSON: `docs/openapi.json`
- Postman collection: `postman/badran-store-api.postman_collection.json`
