# AI Coding Agent Instructions for `spring-security`

## Project Overview

Spring Security REST API with JWT authentication and email verification. Java 17 + Spring Boot 3.5.7 application running on MySQL (Docker). Implements user registration, email-based activation, and role-based access control (RBAC). The codebase is located in `sec-service/` subdirectory with standard Spring Boot structure.

## Architecture & Data Flow

### Layered Request Flow

1. **Controller Layer** (`controller/`): Receives HTTP requests, validates DTOs, orchestrates auth
2. **Service Layer** (`service/`): Business logic for users, roles, validation tokens, notifications
3. **Security Layer** (`securite/`): JWT token generation (`JwtService`), request filtering (`JwtFilter`), Spring Security config
4. **Repository Layer** (`repository/`): Spring Data JPA for database queries
5. **Entity Layer** (`sec/entities/`): Domain models with JPA annotations

### Authentication Flow

```
POST /inscription (InscriptionDTO)
  → AccountServiceImplementation.AddNewUser() + ValidationService.saveValidation()
  → Validation token sent via MailConfig
POST /activation (code)
  → ValidationService validates code (expires after 30 min)
  → User marked active, can now authenticate
POST /connexion (AuthentificationDTO)
  → AuthenticationManager validates credentials
  → JwtService.generate(username) returns JWT token
Subsequent requests
  → JwtFilter extracts "bearer" token from Authorization header
  → Validates token, sets SecurityContext
```

### Key Components

- **AppUser**: Implements `UserDetails`, manages roles via `@ManyToMany` with `AppRole`
- **AppRole**: `GrantedAuthority`, linked to users via join table
- **InscriptionDTO/AuthentificationDTO**: Records (Java 16+) for request data
- **ValidationService**: Manages email verification codes (30-min expiry)
- **NotificationService**: Email dispatch via `MailConfig` (Spring Mail)

## Developer Workflows

### Build & Run

- **Build**: `cd sec-service && mvnw.cmd clean install` (Windows) or `./mvnw clean install`
- **Run**: `mvnw spring-boot:run` (starts on `http://localhost:8081/api`, requires MySQL running via `docker-compose up`)
- **Test**: `mvnw test` (tests in `src/test/java/`)
- **Debug**: Set breakpoints; run with IDE debug configuration or `mvnw spring-boot:run -Dagentlib:jdwp`

### Key Configuration Files

- `application.properties`: DB URL (jdbc:mysql://mysql:3306/sec_service_db), server port 8081, context path `/api`, Hibernate DDL=update
- `docker-compose.yml`: Defines MySQL service
- `pom.xml`: Java 17 target, Spring Boot 3.5.7, JJWT 0.11.5, Lombok, Spring Mail

## Naming & Conventions

| Component         | Suffix                  | Example                        | Location        |
| ----------------- | ----------------------- | ------------------------------ | --------------- |
| Controller        | `Controller`            | `AccountController`            | `controller/`   |
| Service Interface | `Service`               | `AccountService`               | `service/`      |
| Service Impl      | `ServiceImplementation` | `AccountServiceImplementation` | `service/`      |
| Repository        | `Repository`            | `AppUserRepository`            | `repository/`   |
| DTO               | `DTO`                   | `InscriptionDTO`               | `dto/`          |
| Entity            | None                    | `AppUser`, `AppRole`           | `sec/entities/` |

**Security Marker**: Use `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` on sensitive fields like password (in `AppUser`).

## Critical Patterns & Quirks

### SecurityConfig Public Endpoints

Only 4 routes are publicly accessible; all others require authentication:

```java
.requestMatchers("/inscription", "/activation", "/desactivation", "/connexion").permitAll()
```

All other endpoints require JWT token in `Authorization: bearer <token>` header.

### Service Layer Transaction & User Loading

- `AccountServiceImplementation` implements both `AccountService` and `UserDetailsService`
- `@Transactional` on class ensures lazy-loading of `AppRole` collections works in `UserDetailsService.loadUserByUsername()`
- `AddRoleToUser()` modifies entities in-memory; Hibernate auto-flushes on transaction commit

### DTO Records vs Entities

- **Records** (AuthentificationDTO, InscriptionDTO): Immutable, used for request/response binding, lightweight
- **Entities** (AppUser, AppRole): Implement Spring Security interfaces, carry persistence metadata, never return directly in sensitive contexts (use DTOs instead)

### Password Encoding

- Configured in `SecServiceApplication` bean: `BCryptPasswordEncoder`
- Always encode passwords on `AddNewUser()` before persistence (see `AccountServiceImplementation` line ~41)

### Email & Validation Token Flow

- On registration: `ValidationService.saveValidation(appUser)` generates code, `NotificationService` sends via `MailConfig`
- Token expires 30 minutes after creation; check `Validation` entity schema
- `activation` endpoint looks up code in `ValidationRepository`, verifies expiry, activates user

## Integration Points

| System     | Config                                     | Details                                             |
| ---------- | ------------------------------------------ | --------------------------------------------------- |
| **MySQL**  | `application.properties`                   | JDBC URL, credentials, Hibernate dialect            |
| **Email**  | `MailConfig` class + `spring.mail.*` props | Sends activation codes via SMTP                     |
| **Docker** | `docker-compose.yml`                       | MySQL 8.0 service for local dev                     |
| **JWT**    | `JwtService` hardcoded key                 | 256-bit key in `KEYENCRYPT` constant; 30-min expiry |

**Warning**: JWT key in `JwtService` is hardcoded—rotate in production via environment variables.

## Test & Common Issues

- **Tests**: Add to `src/test/java/com/zidane/secservice/` following existing pattern in `SecServiceApplicationTests`
- **Common Issues**:
  - DB connection fails: Ensure `docker-compose up` running, MySQL port 3306 accessible
  - JWT validation fails: Check token expiry (30 min), verify secret key matches, confirm header format is `bearer <token>`
  - User activation fails: Verify email provider configured in `MailConfig`; check `Validation` table for code TTL logic
