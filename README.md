# Movie Library

Secure Spring Boot application for managing a movie catalog with asynchronous rating enrichment.

## Features
- CRUD operations for movies
- Asynchronous IMDb rating enrichment via OMDb
- Role-based security (ADMIN / USER)
- Swagger API documentation
- Unit and controller tests

## Running the Application


### Prerequisites
- Java 17+
- Maven
- MariaDB (or compatible database)

1. Set environment variable: OMDB_API_KEY=your_key_here
2. Run the application: ./mvnw spring-boot:run
3. Application starts on: http://localhost:8080

---

## API Documentation
Swagger UI available at:
http://localhost:8080/swagger-ui.html

You can explore all endpoints and test requests directly from the browser.

---

## Security
The API is secured using **Spring Security (HTTP Basic Authentication)**.

### Roles & Permissions

| Role  | Permissions |
|------|------------|
| USER | Read-only access (GET endpoints) |
| ADMIN | Full CRUD access |

### Test Credentials

| Role  | Username | Password |
|------|----------|----------|
| USER | user | user123 |
| ADMIN | admin | admin123 |

---

## Asynchronous Rating Enrichment

- Movies are created immediately with a `null` rating
- Rating is fetched asynchronously from OMDb using the movie title
- External API failures do not affect the main request
- Rating is updated later if available

---

## Technical Documentation

A detailed technical overview is available here:

docs/technical-overview.md

This document explains:
- External API integration
- Security design
- Asynchronous processing
- Architectural decisions and trade-offs

---

## Notes & Future Improvements

- Rating is currently fetched by movie title only
- Release year can be included for better consistency
- Retry/caching can be added for external API calls
- JWT-based authentication can replace HTTP Basic if needed

---

## License

This project is intended for educational purposes.