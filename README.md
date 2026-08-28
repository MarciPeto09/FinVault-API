# FinVault API

FinVault API is a Spring Boot backend for secure document management. 
It uses Amazon Cognito for authentication, Amazon S3 for file storage, and PostgreSQL for document metadata.

## Features

- Cognito JWT authentication
- Authenticated document management
- Ownership-based access control
- Multipart document uploads
- Private Amazon S3 storage
- Presigned download URLs
- PostgreSQL persistence with Spring Data JPA
- H2-backed test configuration
- CORS support for the frontend application

## Technology Stack

- Java 21
- Spring Boot 4.1.0
- Spring Security OAuth2 Resource Server
- Spring Data JPA and Hibernate
- PostgreSQL
- H2
- AWS SDK for Java 2.x
- Amazon S3
- Amazon Cognito
- Maven

## Project Structure

```text
src/
├── main/
│   ├── java/com/Marci/FinVault/API/
│   │   ├── config/        # Security and AWS configuration
│   │   ├── controller/    # REST endpoints
│   │   ├── entity/        # JPA entities
│   │   ├── enums/         # Domain enumerations
│   │   ├── repository/    # Database repositories
│   │   └── service/       # Application and storage services
│   └── resources/
│       └── application.properties
└── test/
    ├── java/              # Automated tests
    └── resources/         # Test configuration