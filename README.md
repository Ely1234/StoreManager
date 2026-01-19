# Store Management API (Spring Boot)

Backend-only Store Management API built with Java 17, Spring Boot 3, Spring Data JPA (H2) and Spring Security (HTTP Basic).

## Requirements
- Java 17+
- Maven 3.9+

## Run locally

```bash
mvn spring-boot:run
```

API base URL: http://localhost:8080

## Authentication

HTTP Basic users (in-memory for this exercise):
- admin / adminpass (ROLE_ADMIN)
- user / userpass (ROLE_USER)

## Role-based access
- ROLE_USER can read (GET)
- ROLE_ADMIN can read + write (POST, PATCH, DELETE)

## Endpoints

All endpoints are under `/api/products`.

Create product (ADMIN)

```bash
curl -i -u admin:adminpass \
  -H "Content-Type: application/json" \
  -d '{"sku":"SKU-001","name":"Coffee","description":"500g","price":19.99,"currency":"RON","quantity":10}' \
  http://localhost:8080/api/products
```

List products (USER or ADMIN)

```bash
curl -i -u user:userpass http://localhost:8080/api/products?page=0&size=20
```

Get by id (USER or ADMIN)

```bash
curl -i -u user:userpass http://localhost:8080/api/products/<uuid>
```

Get by SKU (USER or ADMIN)

```bash
curl -i -u user:userpass http://localhost:8080/api/products/by-sku/SKU-001
```

Update price (ADMIN)

```bash
curl -i -u admin:adminpass \
  -H "Content-Type: application/json" \
  -d '{"price":25.50}' \
  http://localhost:8080/api/products/<uuid>/price
```

Delete (ADMIN)

```bash
curl -i -u admin:adminpass -X DELETE http://localhost:8080/api/products/<uuid>
```

## Error handling

The API returns RFC7807 `application/problem+json` responses for common errors (validation, not found, conflict).

## Tests

Run tests:

```bash
mvn test
```


git add src/main/java/com/example/store/StoreManagementApiApplication.java
git commit -m "feat: add Spring Boot entrypoint"

git remote add origin <your-github-repo-url>
git push -u origin main
```
