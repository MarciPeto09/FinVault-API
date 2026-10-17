# FinVault-API

FinVault-API is the backend service for the FinVault platform, a document and records management system designed to help users securely store, search, and manage financial or business documents.

Although this repository is the API layer rather than a browser UI, it powers the front-end experience by exposing secure endpoints for login, document management, search, and AWS-backed storage.

## What the frontend does

The frontend built on top of this API would typically provide the following user workflows:

- User sign-in and authentication
- Secure document upload and download
- Search for documents by name, tag, type, or metadata
- View document details such as status, version, retention policy, and compliance items
- Manage uploaded files and track who uploaded or updated them
- Review audit history and access activity
- Browse a document dashboard with filtering and pagination

In short, the app is designed to act like a secure document vault for business records, with the API handling authentication, file storage, metadata persistence, and search logic.

## Main features in this repository

### Authentication and user access
- User entity with email/password storage
- Password hashing with Spring Security BCrypt
- Login endpoint at `/api/auth/login`
- Role-based access support through security configuration

### Document management
- Document entity storing metadata such as:
  - name
  - path
  - size
  - type
  - status
  - tags
  - version
  - checksum
  - retention policy
  - compliance and encryption details
- Support for document metadata models and repository access

### Search capabilities
- Search controller and search service ready for document lookup logic
- Search requests/responses are modeled in DTOs so the frontend can query and display results cleanly

### AWS / storage integration
- AWS configuration is defined in the project
- Storage services for S3-compatible object storage are included
- Presigned URL support indicates the application is designed to securely expose upload/download links to the frontend without exposing raw credentials

### Audit and monitoring
- Audit log support is included for tracking actions across the platform
- This is useful for compliance-heavy document management workflows

## Technology stack

- Java 25
- Spring Boot 4.1
- Spring Web MVC
- Spring Security
- Spring Data JPA
- AWS integration for S3-backed storage
- Maven

## Project structure

- `src/main/java/com/Marci/FinVault/API/controller` — REST API controllers
- `src/main/java/com/Marci/FinVault/API/service` — business logic for auth, search, storage, and docs
- `src/main/java/com/Marci/FinVault/API/entity` — JPA entities
- `src/main/java/com/Marci/FinVault/API/repository` — database access layers
- `src/main/java/com/Marci/FinVault/API/config` — security and AWS configuration
- `src/main/resources` — application configuration

## Running the app

From the project root:

```bash
./mvnw spring-boot:run
```

Or on Windows:

```bash
mvnw.cmd spring-boot:run
```

## Notes

This project appears to be in an early stage of development. Several controllers and services are scaffolded but may still require completion and validation. The overall architecture strongly suggests a secure document management platform that is intended to support a richer front-end dashboard.

## Summary

FinVault-API is the backend foundation for a secure document vault application. It handles user authentication, document records, metadata storage, AWS file access, and search features that a frontend dashboard would use to let users securely manage financial and business documents.
