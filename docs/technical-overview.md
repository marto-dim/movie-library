# Movie Library – Technical Overview

## 1. External Rating API

This project integrates with the **OMDb API** to retrieve movie rating data.

### Why OMDb?
- Public and easy-to-use REST API
- Requires minimal parameters (movie title)
- Provides IMDb rating, which is widely recognized

### Integration Approach
- Spring Cloud **OpenFeign** is used for HTTP communication
- A dedicated Feign client (`OmdbClient`) handles external calls
- API credentials are injected via environment variables to avoid hardcoding secrets

The rating is retrieved using the movie title after the movie is created.

---

## 2. Authentication & Authorization

The application is secured using **Spring Security**.

### Roles
- **ADMIN**
    - Full CRUD access to movie resources
- **USER**
    - Read-only access (GET endpoints)

### Security Configuration
- HTTP Basic authentication is used for simplicity
- Role-based access is enforced at the endpoint level
- Unauthorized or forbidden access results in proper HTTP status codes (401 / 403)

---

## 3. Asynchronous Rating Enrichment

To avoid blocking client requests, rating enrichment is performed asynchronously.

### Workflow
1. Client sends a POST request to create a movie
2. Movie is persisted immediately with a `null` rating
3. A background task is triggered to fetch rating data from OMDb
4. If a rating is found, the movie record is updated asynchronously

### Implementation Details
- `@Async` is used to execute the enrichment in a separate thread
- Failures in the external API do not affect the main request
- Exceptions are safely swallowed to ensure system stability

This ensures fast response times and resilience against external service outages.

---

## 4. Architecture & Design Decisions

### Layered Architecture
The application follows a classic layered design:
- **Controller layer** – REST endpoints and request validation
- **Service layer** – Business logic and orchestration
- **Repository layer** – Persistence using Spring Data JPA
- **Client layer** – External API integration (OMDb)

### DTO Usage
- DTOs are used for input and output to decouple API contracts from entities
- Mapping is handled via dedicated mapper classes

### Validation & Error Handling
- Bean Validation (`jakarta.validation`) ensures valid input
- Global exception handling provides consistent error responses

---

## 5. Trade-offs & Future Improvements

### Current Trade-offs
- Rating is fetched only by movie title, which may lead to ambiguities
- No retry mechanism for failed external calls
- HTTP Basic authentication is used instead of JWT for simplicity

### Possible Improvements
- Include release year when querying the external API
- Add retry/backoff logic for external API calls
- Introduce JWT-based authentication
- Cache external API responses to reduce load
- Add integration tests

---

## Conclusion

The application demonstrates a secure, scalable, and maintainable Spring Boot architecture with asynchronous external data enrichment. It balances simplicity and robustness while leaving room for future enhancements.
