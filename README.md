# Movie Library

Secure Spring Boot application for managing a movie catalog with asynchronous rating enrichment.

## Features
- CRUD operations for movies
- Asynchronous IMDb rating enrichment via OMDb
- Role-based security (ADMIN / USER)
- Swagger API documentation
- Unit and controller tests

## Running the Application

1. Set environment variable: OMDB_API_KEY=your_key_here
2. Run the application: ./mvnw spring-boot:run

## API Documentation
Swagger UI available at:
http://localhost:8080/swagger-ui.html

## Security
- ADMIN: full access
- USER: read-only access