# ecommerce-springboot

Spring Boot backend for the E-Commerce app (Auth + Products).

Run with PostgreSQL:

1. Create the databases:

```sql
CREATE DATABASE ecommerce;
CREATE DATABASE ecommerce_test;
```

2. Configure connection values if your PostgreSQL setup differs from the defaults:

```bash
DB_HOST=localhost
DB_PORT=5432
DB_NAME=ecommerce
DB_USER=postgres

.
DB_PASSWORD=postgres

TEST_DB_NAME=ecommerce_test
```

3. Start the backend:

```
mvnw spring-boot:run
```

Server runs on port 8081 and uses PostgreSQL for development, tests, and production.

Quick curl examples:

- Register:

```bash
curl -X POST http://localhost:8081/api/auth/register \
	-H "Content-Type: application/json" \
	-d '{"name":"Test","email":"test@example.com","password":"pass123"}'
```

- Login:

```bash
curl -X POST http://localhost:8081/api/auth/login \
	-H "Content-Type: application/json" \
	-d '{"email":"test@example.com","password":"pass123"}'
```

- Create product (use token from login response):

```bash
curl -X POST http://localhost:8081/api/products \
	-H "Content-Type: application/json" \
	-H "Authorization: Bearer <TOKEN>" \
	-d '{"name":"Sample","description":"Nice","price":9.99}'
```

- List products (authorized):

```bash
curl -H "Authorization: Bearer <TOKEN>" http://localhost:8081/api/products
```

