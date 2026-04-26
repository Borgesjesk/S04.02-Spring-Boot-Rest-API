# 🍎 S04.02 - Fruit Inventory API (Level 1: H2 & Docker)

A production-ready REST API built with Spring Boot for managing fruit inventory. This project is the first level of a three-level exercise covering H2, MySQL, and MongoDB. It was built following TDD (Test-Driven Development), with every endpoint tested before implementation, and packaged in an optimized Docker container.

## 📋 What This Project Does

This is a Fruit Inventory API that allows you to register fruits with their name and weight, list all fruits, search by ID, update entries, and delete them. It runs on port 9000 with an in-memory H2 database.

The API enforces strict input validation, returns proper HTTP status codes for every scenario, uses DTOs to decouple the API contract from the database schema, and handles all errors through a centralized GlobalExceptionHandler. Every endpoint was developed test-first following TDD methodology, resulting in 18 automated tests covering happy paths, validation failures, and edge cases.

## 🧠 What I Learned Building This

This was my first time implementing DTOs, Bean Validation, global error handling, and Docker containerization. These are the key concepts I applied:

- **DTO Pattern (Data Transfer Objects)**: Instead of exposing JPA entities directly to the API (a security risk), I created `FruitRequestDto` and `FruitResponseDto` to decouple the API contract from the persistence layer. The client never sees or controls internal fields like the database-generated `id`.
- **Bean Validation with Defensive Programming**: Every input is treated as potentially hostile. `@NotBlank` rejects empty and whitespace-only names. `@NotNull` + `@Positive` on `Integer weightInKilos` catches null, zero, and negative values — using the wrapper type `Integer` instead of primitive `int` so we can distinguish "missing field" from "zero value".
- **Global Exception Handling**: `@RestControllerAdvice` intercepts exceptions across all controllers. `FruitNotFoundException` returns a structured 404. `MethodArgumentNotValidException` returns a 400 with field-level error details — the client knows exactly which field failed and why.
- **Mapper Utility Class**: A dedicated `FruitMapper` handles all Entity ↔ DTO conversions with null-safety checks and a private constructor to prevent instantiation. Keeps conversion logic out of controllers and services.
- **TDD (Test-Driven Development)**: Every feature was built in the Red-Green cycle: write a failing test → implement the minimum code to pass → refactor. The Git history reflects this with `(red)` and `(green)` markers in commit messages.
- **Unit Testing with Mockito**: Service layer tested in isolation by mocking the repository. Verified both happy paths (create returns saved fruit) and unhappy paths (getFruitById throws exception for missing ID).
- **Docker Multi-Stage Build**: Stage 1 compiles the application with Maven/JDK. Stage 2 copies only the `.jar` to a slim JRE image. The container runs as a non-root user (`springuser`) following the Principle of Least Privilege — if the app is compromised, the attacker doesn't get root access.

## 🛠️ Technologies

- Java 21 (Temurin LTS)
- Spring Boot 4.0.6
- Spring Data JPA + H2 Database
- Maven (wrapper included — no local Maven installation needed)
- Bean Validation (Hibernate Validator)
- Lombok
- JUnit 5 + Mockito + MockMvc
- Docker (Multi-stage production build)
- IntelliJ IDEA

## 📁 Project Structure

The project follows a layered MVC architecture with clear separation of concerns:

```
src/main/java/cat/itacademy/s04/t02/n01/fruit_api_h2/
│
├── controller/                   # Receives HTTP requests, returns ResponseEntity
│   └── FruitController.java      # CRUD endpoints: POST, GET, PUT, DELETE /fruits
│
├── dto/                          # Data Transfer Objects — API contract layer
│   ├── FruitRequestDto.java      # Input: name + weightInKilos with validation
│   └── FruitResponseDto.java     # Output: id + name + weightInKilos
│
├── exception/                    # Centralized error handling
│   ├── FruitNotFoundException.java    # Custom RuntimeException for missing fruits
│   └── GlobalExceptionHandler.java   # @RestControllerAdvice — catches and formats errors
│
├── mapper/                       # Entity ↔ DTO conversion utilities
│   └── FruitMapper.java          # Static methods with null-safety, private constructor
│
├── model/                        # JPA persistence entities
│   └── Fruit.java                # @Entity with @Id, @GeneratedValue, custom constructor
│
├── repository/                   # Data access layer
│   └── FruitRepository.java      # Interface extending JpaRepository<Fruit, Long>
│
├── service/                      # Business logic layer
│   ├── FruitService.java         # Interface defining CRUD operations
│   └── FruitServiceImpl.java     # Implementation with DTO mapping and exception handling
│
└── FruitApiH2Application.java    # Spring Boot entry point
```

```
src/test/java/cat/itacademy/s04/t02/n01/fruit_api_h2/
│
├── FruitControllerIntegrationTest.java  # Full-stack endpoint tests (14 tests)
├── FruitServiceTest.java                # Mockito service unit tests (4 tests)
└── FruitApiH2ApplicationTests.java      # Context load test
```

## 🔌 API Endpoints

### Fruit Management

| Method | Endpoint | Description | Success | Error |
|--------|----------|-------------|---------|-------|
| POST | `/fruits` | Create a new fruit entry | 201 Created | 400 Bad Request |
| GET | `/fruits` | Retrieve all fruits (empty array if none) | 200 OK | — |
| GET | `/fruits/{id}` | Retrieve a specific fruit by ID | 200 OK | 404 Not Found |
| PUT | `/fruits/{id}` | Update an existing fruit | 200 OK | 404 / 400 |
| DELETE | `/fruits/{id}` | Remove a fruit by ID | 204 No Content | 404 Not Found |

### Error Response Format

All errors return a consistent JSON structure:

**404 Not Found:**
```json
{
    "status": 404,
    "error": "Not Found",
    "message": "Fruit with ID: 999 not found"
}
```

**400 Validation Error:**
```json
{
    "status": 400,
    "error": "Validation Failed",
    "errors": {
        "name": "Fruit name cannot be empty",
        "weightInKilos": "Weight must be greater than zero"
    }
}
```

## ⚙️ How to Run

### Option A: Docker (Recommended)

```bash
docker build -t fruit-api-h2 .
docker run -p 9000:9000 fruit-api-h2
```

### Option B: Local Execution (Maven)

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:9000`

### Run Tests

```bash
./mvnw test
```

## 💻 API Usage Examples

### Create a fruit

```bash
curl -X POST http://localhost:9000/fruits \
  -H "Content-Type: application/json" \
  -d '{"name": "Tangerine", "weightInKilos": 2}'
```

Response (201 Created):
```json
{
    "id": 1,
    "name": "Tangerine",
    "weightInKilos": 2
}
```

### Get all fruits

```bash
curl http://localhost:9000/fruits
```

### Get fruit by ID

```bash
curl http://localhost:9000/fruits/1
```

### Update a fruit

```bash
curl -X PUT http://localhost:9000/fruits/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Green Tangerine", "weightInKilos": 3}'
```

### Delete a fruit

```bash
curl -X DELETE http://localhost:9000/fruits/1
```

Response: 204 No Content

### Try invalid input

```bash
curl -X POST http://localhost:9000/fruits \
  -H "Content-Type: application/json" \
  -d '{"name": "", "weightInKilos": -1}'
```

Response (400 Bad Request):
```json
{
    "status": 400,
    "error": "Validation Failed",
    "errors": {
        "name": "Fruit name cannot be empty",
        "weightInKilos": "Weight must be greater than zero"
    }
}
```

## 🧪 Test Coverage

| Metric | Count |
|--------|-------|
| Total Tests | 18 (0 failures) |
| Integration Tests | 14 |
| Unit Tests (Mockito) | 4 |

### Tests by Layer

**🔗 Integration Tests (FruitControllerIntegrationTest — 14 tests):**
- Create fruit with valid data returns 201, blank name returns 400, zero weight returns 400, negative weight returns 400, get all fruits returns list, get all when empty returns empty array, get by valid ID returns 200, get by invalid ID returns 404, update with valid data returns 200, update non-existing ID returns 404, update with blank name returns 400, delete existing fruit returns 204 and confirms removal, delete non-existing ID returns 404

**🧩 Service Unit Tests (FruitServiceTest — 4 tests):**
- Create fruit returns saved entity with ID, get by valid ID returns fruit, get by non-existing ID throws FruitNotFoundException, delete non-existing ID throws FruitNotFoundException and never calls deleteById

## 🏗️ Architecture & Design Decisions

- **DTO over Entity exposure**: The client never interacts with JPA entities. `FruitRequestDto` controls what data enters the system (no `id` field — the database owns that). `FruitResponseDto` controls what data leaves. This prevents mass assignment attacks and decouples the API contract from the database schema.
- **Integer over int for DTO validation**: Using the wrapper type `Integer` for `weightInKilos` in the DTO allows `@NotNull` to detect missing fields. A primitive `int` would silently default to `0`, hiding client errors.
- **existsById() for delete**: Instead of `findById()` + `orElseThrow()`, the delete operation uses `existsById()` — more efficient because it doesn't load the entire entity into memory just to delete it.
- **Static mapper with private constructor**: `FruitMapper` is a stateless utility class. The private constructor with `UnsupportedOperationException` prevents instantiation. Static methods keep it simple — no Spring dependency injection needed for pure data conversion.
- **@Transactional on integration tests**: Each test runs in a transaction that rolls back after execution, ensuring test isolation. No test leaks data into another test.
- **Non-root Docker container**: The production image runs as `springuser`, not root. This follows the Principle of Least Privilege — a core cybersecurity concept that limits the blast radius if the application is compromised.

## 🐳 Dockerfile Explained

```dockerfile
# Stage 1: Build — compile source code and generate .jar
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline          # Cache dependencies in a separate layer
COPY src ./src
RUN mvn clean package -DskipTests      # Compile and package (tests already ran in CI)

# Stage 2: Production — minimal runtime image
FROM eclipse-temurin:21-jre-jammy
RUN useradd -ms /bin/bash springuser   # Create non-root user
USER springuser                         # Switch to non-root
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar  # Copy only the .jar from Stage 1
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"] # Exec form — receives OS signals directly
```

## 📈 Potential Improvements

- `weightInKilos` could be `double` instead of `int` for more precise measurements (e.g., 2.5 kg). Noted as a design constraint from the exercise specification.
- Additional Mockito tests for `findAllFruits()` and `updateFruit()` to increase service layer coverage.
- Add `spring.jpa.open-in-view=false` to disable the open-in-view anti-pattern.
- Integration with Swagger/OpenAPI for interactive API documentation.

## 📈 Roadmap

- **Level 2**: Migration to MySQL with `@ManyToOne` relationship between Fruit and Provider entities. Docker Compose for multi-container orchestration. Database credentials managed via environment variables.
- **Level 3**: MongoDB persistence with embedded subdocuments for managing fruit orders.