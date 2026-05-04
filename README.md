# Flight Reservation System

A RESTful Spring Boot API for managing airline flight bookings — customers can register, search for flights, book seats, and cancel reservations. Wallet funds are deducted atomically on booking.

---

## Technology Stack

| Layer | Technology | Version |
|-------|------------|---------|
| Framework | Spring Boot | 3.4.13 |
| Language | Java | 21 (LTS) |
| Database | PostgreSQL | 15+ |
| Schema migrations | Flyway | (managed by Spring Boot BOM) |
| ORM | Spring Data JPA / Hibernate | (managed by Spring Boot BOM) |
| Object mapping | MapStruct | 1.5.5.Final |
| Boilerplate reduction | Lombok | 1.18.38 |
| API documentation | springdoc-openapi (Swagger UI) | 2.8.8 |
| Observability | Spring Boot Actuator | (managed by Spring Boot BOM) |
| CVE scanning | OWASP Dependency-Check | 12.1.0 |
| SBOM generation | CycloneDX Maven Plugin | 2.9.1 |
| Build tooling | Apache Maven Wrapper | 3.9.9 |

---

## Architecture Overview

The project follows a strict layered architecture. Every layer only depends on the layer directly below it — controllers never touch repositories, services never reference other service implementations directly.

```
┌──────────────────────────────────────────────┐
│               REST Controllers               │  ← HTTP in/out, validation, @Valid
│  FlightController  CustomerController  ...   │
└───────────────────┬──────────────────────────┘
                    │ calls interface (DIP)
┌───────────────────▼──────────────────────────┐
│             Service Interfaces               │  ← FlightService, CustomerService, ...
│             Service Implementations          │  ← @Transactional, business logic
└───────────────────┬──────────────────────────┘
                    │
┌───────────────────▼──────────────────────────┐
│         Spring Data JPA Repositories         │  ← JpaRepository, derived queries
└───────────────────┬──────────────────────────┘
                    │
┌───────────────────▼──────────────────────────┐
│              PostgreSQL Database             │  ← Schema managed by Flyway
└──────────────────────────────────────────────┘

Cross-cutting:
  • AOP LoggingAdvice      — logs every create* method call and response
  • RestExceptionHandler   — @ControllerAdvice mapping exceptions → HTTP status codes
  • MapStruct Mappers      — compile-time Entity ↔ DTO conversion (no reflection)
```

**Spring profiles:**
- `dev` — SQL logging enabled, Swagger UI enabled
- `prod` — SQL logging off, Swagger UI disabled
- `test` — H2 in-memory database, Flyway disabled

---

## Domain Model

```
Supplier ──< Plane >── Flight ──< Booking >── Customer
                         │                        │
                      City (from)              Wallet
                      City (to)
```

| Entity | Table | Purpose |
|--------|-------|---------|
| `Supplier` | `tb_suppliers` | Airline company that owns planes |
| `Plane` | `tb_planes` | Aircraft with model and passenger capacity |
| `City` | `tb_cities` | Departure/destination city with GPS coordinates |
| `Flight` | `tb_flights` | Scheduled flight between two cities on a specific date |
| `Customer` | `tb_customer` | Registered passenger with contact details |
| `Wallet` | `tb_wallets` | Customer's balance for purchasing tickets |
| `Booking` | `tb_booking` | Join table linking customers to flights they've booked |

---

## API Endpoints

All endpoints are under `/api/v1`. Full interactive documentation is at `http://localhost:8001/swagger-ui.html`.

### Suppliers
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/supplier/create` | Register a new airline supplier |

**Example:** `{"supplierName": "Delta Air Lines"}`

### Planes
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/plane/create` | Add a new plane |

**Example:** `{"model": "Boeing 737", "capacity": 180, "supplier": "Delta Air Lines"}`

### Cities
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/city/create` | Register a city |

**Example:** `{"cityName": "New York", "state": "NY", "lat": 40.712776, "lon": -74.005974}`

### Customers
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/customer/create` | Register a new customer with initial wallet balance |
| GET | `/api/v1/customer/get/{id}` | Get customer details including flight history |

**Example create:** `{"email": "jane@example.com", "firstName": "Jane", "lastName": "Doe", "phoneNumber": "1234567890", "funds": 500.00}`

### Flights
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/flight/create` | Create a new flight |
| GET | `/api/v1/flight/find-all?date=YYYY-MM-DD&from=CityName&to=CityName` | Search available flights |
| POST | `/api/v1/flight/book` | Book a flight for a customer (deducts wallet funds) |
| DELETE | `/api/v1/flight/delete` | Cancel a booking |

**Example book/cancel:** `{"customerId": 1, "flightId": 1}`

### Error responses

| Situation | HTTP Status |
|-----------|-------------|
| Entity not found | 404 Not Found |
| Flight fully booked | 409 Conflict |
| Insufficient wallet funds | 406 Not Acceptable |
| Invalid request body | 400 Bad Request (field-level errors) |

---

## Setup & Running

### Prerequisites

- Java 21+ (`java -version`)
- PostgreSQL 15+ running locally

### 1. Create the database

```bash
psql -U postgres -c "CREATE DATABASE flight_reservation_master;"
```

### 2. Configure credentials (optional)

The application uses environment variables with safe defaults:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/flight_reservation_master
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
```

If not set, defaults from `application.properties` are used.

### 3. Run with the dev profile

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Flyway will run `V1__init_schema.sql` automatically on first start, creating all 7 tables.

### 4. Verify it's running

| URL | Purpose |
|-----|---------|
| `http://localhost:8001/swagger-ui.html` | Interactive API docs |
| `http://localhost:8001/actuator/health` | Health check → `{"status":"UP"}` |
| `http://localhost:8001/actuator/info` | Build info |

---

## Best Practices Used

### SOLID Principles
- **Single Responsibility** — `CustomerController` assembles the customer response from two services; services handle only their own domain logic
- **Open/Closed** — New entity types can be added without modifying existing services
- **Dependency Inversion** — All service dependencies are injected via interfaces (`FlightService`, `CustomerService`), never concrete implementation classes

### Data & Transactions
- **`@Transactional`** on every write method; **`@Transactional(readOnly = true)`** on every read — prevents dirty reads and gives the JPA provider optimization hints
- **Flyway** for schema versioning — schema changes are tracked as SQL migration scripts, not inferred from entity annotations
- **`spring.jpa.open-in-view=false`** — prevents lazy-loading across HTTP request boundaries

### Input Validation
- **Jakarta Bean Validation** (`@NotNull`, `@NotBlank`, `@Positive`, `@Email`) on all request classes
- **`@Valid`** on every `@RequestBody` in every controller
- **Global `@ControllerAdvice`** returns structured field-level error maps on validation failure (400)

### Mapping
- **MapStruct** with `componentModel = "spring"` — compile-time code generation, zero reflection, full IDE refactor support; mappers injected as Spring beans

### Observability
- **AOP `LoggingAdvice`** logs all `create*` method arguments and return values without touching business code
- **Spring Boot Actuator** exposes `health`, `info`, and `metrics` endpoints

### API
- **springdoc-openapi** with `@Tag` and `@Operation` annotations on all controllers — Swagger UI available in dev profile

### Security & Supply Chain
- **OWASP Dependency-Check** — every dependency (direct + transitive) is scanned against the NVD CVE database; builds fail on CVSS ≥ 7
- **CycloneDX SBOM** — `./mvnw package` generates `target/bom.json`, a machine-readable inventory of all components
- **Dependabot** — configured to open weekly PRs for outdated dependencies and immediate PRs for CVEs
- **Maven Wrapper pinned** — SHA-256 checksum on the Maven binary prevents supply chain tampering
- **Reproducible builds** — `project.build.outputTimestamp` ensures byte-identical JARs across environments
- **No hardcoded credentials** — all secrets loaded from environment variables

### Spring Profiles
- `dev` — verbose SQL logging, Swagger enabled
- `prod` — silent, Swagger disabled
- `test` — H2 in-memory database, Flyway disabled, isolated from production data

---

## Running Tests & Security Scans

```bash
# Unit and integration tests
./mvnw test

# Check for outdated dependency versions
./mvnw versions:display-dependency-updates
./mvnw versions:display-plugin-updates

# CVE vulnerability scan (downloads NVD data on first run — takes a few minutes)
./mvnw dependency-check:check
# Report: target/dependency-check/dependency-check-report.html

# Generate Software Bill of Materials
./mvnw package -DskipTests
# SBOM: target/bom.json
```

---

