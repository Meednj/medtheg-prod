# Identity Module Architecture

## 1. Overview

The Identity module is responsible for user identity and authentication within `medtheg-prod`.

Its current responsibilities are:

* User registration
* Password hashing and verification
* User authentication
* JWT access-token generation
* User account status management
* Role-based authorization
* User persistence

The module follows the project's **modular monolith + hexagonal architecture** approach.

Identity is implemented as an independent business module alongside Catalog:

```text
com.medthegprod.backend
├── catalog/
├── identity/
└── infrastructure/
    └── security/
```

The Identity module owns user-related business rules, while security implementations such as BCrypt and JWT remain in cross-cutting infrastructure.

---

# 2. Module Structure

```text
identity/
├── domain/
│   ├── model/
│   │   ├── User.java
│   │   ├── UserId.java
│   │   ├── Role.java
│   │   └── UserStatus.java
│   │
│   └── repository/
│       └── UserRepository.java
│
├── application/
│   ├── port/
│   │   ├── PasswordHasher.java
│   │   └── TokenGenerator.java
│   │
│   ├── usecase/
│   │   ├── RegisterUserUseCase.java
│   │   ├── AuthenticateUserUseCase.java
│   │   └── AuthenticationResult.java
│   │
│   └── service/
│       ├── RegisterUserService.java
│       ├── AuthenticateUserService.java
│       ├── UserAlreadyExistsException.java
│       ├── InvalidCredentialsException.java
│       └── UserSuspendedException.java
│
└── infrastructure/
    ├── persistence/
    │   ├── UserPersistenceAdapter.java
    │   ├── entity/
    │   │   └── UserEntity.java
    │   ├── mapper/
    │   │   └── UserMapper.java
    │   └── repository/
    │       └── UserJpaRepository.java
    │
    └── web/
        ├── controller/
        │   ├── AuthController.java
        │   └── IdentityExceptionHandler.java
        ├── dto/
        │   ├── RegisterRequest.java
        │   ├── LoginRequest.java
        │   ├── UserResponse.java
        │   └── AuthResponse.java
        └── mapper/
            └── UserWebMapper.java
```

Cross-cutting security infrastructure:

```text
infrastructure/
└── security/
    ├── BCryptPasswordHasher.java
    ├── JwtKeyConfig.java
    ├── JwtTokenGenerator.java
    └── SecurityConfig.java
```

---

# 3. Domain Model

## 3.1 User

`User` is the main aggregate root of the Identity module.

It contains:

```text
User
├── id
├── email
├── passwordHash
├── role
└── status
```

The domain object is responsible for maintaining basic user invariants.

### Email normalization

Email addresses are normalized by:

1. Removing surrounding whitespace
2. Converting to lowercase

For example:

```text
"  User@Example.COM  "
        ↓
"user@example.com"
```

This prevents logically identical email addresses from being treated as different accounts.

### Password handling

The domain stores only a password hash.

A raw password must never be persisted or returned through the API.

```text
Raw password
     ↓
PasswordHasher
     ↓
passwordHash
     ↓
User
```

The domain does not know which hashing algorithm is used.

This is intentional because password hashing is an infrastructure concern.

---

# 4. User Identity

`UserId` is a value object wrapping a UUID.

```text
UserId
└── UUID
```

New identifiers are generated inside the application when registering users.

The domain does not depend on database-generated identifiers.

---

# 5. Roles

The current system defines two roles:

```text
CUSTOMER
ADMIN
```

### CUSTOMER

Represents a normal store user.

Customers can authenticate and access public storefront resources.

### ADMIN

Represents an administrator responsible for managing store resources.

Administrators can access protected Catalog management operations.

Authorization is enforced by Spring Security.

---

# 6. User Status

Users currently have two states:

```text
ACTIVE
SUSPENDED
```

An active user can authenticate.

A suspended user cannot obtain an access token.

The domain exposes explicit state transitions:

```text
ACTIVE
   │
   └── suspend()
          ↓
     SUSPENDED
          │
          └── activate()
                 ↓
              ACTIVE
```

---

# 7. Repository Port

The domain defines the `UserRepository` interface:

```java
public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId userId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
```

This is a **port**.

The domain/application layer does not depend on Spring Data JPA or PostgreSQL.

Instead:

```text
Application / Domain
        │
        │ UserRepository
        ▼
UserPersistenceAdapter
        │
        ▼
UserJpaRepository
        │
        ▼
PostgreSQL
```

This follows the Dependency Inversion Principle.

---

# 8. Application Layer

The application layer contains use cases and coordinates domain operations.

It does not directly depend on:

* PostgreSQL
* JPA
* BCrypt
* JWT implementation
* HTTP
* Spring MVC

These concerns are provided through ports or infrastructure adapters.

---

# 9. PasswordHasher Port

The application defines:

```java
public interface PasswordHasher {

    String hash(String rawPassword);

    boolean matches(
        String rawPassword,
        String passwordHash
    );
}
```

This abstraction prevents the application from depending directly on BCrypt.

The current adapter is:

```text
PasswordHasher
      ▲
      │ implements
      │
BCryptPasswordHasher
```

If the hashing technology changes later, the registration/authentication services do not need to change.

---

# 10. TokenGenerator Port

The application defines:

```java
public interface TokenGenerator {

    String generate(User user);
}
```

The application therefore knows that it can request an access token, but it does not know how that token is implemented.

Current implementation:

```text
TokenGenerator
      ▲
      │ implements
      │
JwtTokenGenerator
```

This keeps JWT-specific infrastructure outside the Identity application services.

---

# 11. Registration

Registration is exposed through:

```text
POST /api/auth/register
```

The flow is:

```text
HTTP Request
     │
     ▼
AuthController
     │
     ▼
RegisterUserUseCase
     │
     ▼
RegisterUserService
     │
     ├── normalize email
     │
     ├── check existing email
     │
     ├── PasswordHasher.hash()
     │
     ├── create User
     │
     └── UserRepository.save()
             │
             ▼
        PostgreSQL
```

The application creates new users with:

```text
Role = CUSTOMER
Status = ACTIVE
```

unless a future administrative provisioning mechanism introduces another workflow.

---

# 12. Duplicate Email Protection

Registration checks whether an email already exists:

```text
existsByEmail(email)
        │
        ├── true  → UserAlreadyExistsException
        │
        └── false → continue registration
```

The database also enforces uniqueness:

```sql
CONSTRAINT uk_users_email UNIQUE (email)
```

The application-level check provides a clear business error, while the database constraint provides the final persistence-level guarantee.

---

# 13. Password Security

Passwords are hashed using BCrypt.

The raw password follows this path:

```text
HTTP request
     │
     ▼
RegisterUserService
     │
     ▼
PasswordHasher
     │
     ▼
BCryptPasswordHasher
     │
     ▼
BCrypt hash
     │
     ▼
User.passwordHash
     │
     ▼
Database
```

The raw password is never stored.

During authentication:

```text
raw password
     │
     ▼
BCryptPasswordHasher.matches()
     │
     ▼
stored password hash
```

---

# 14. Authentication

Authentication is exposed through:

```text
POST /api/auth/login
```

The flow is:

```text
LoginRequest
     │
     ▼
AuthController
     │
     ▼
AuthenticateUserService
     │
     ├── find user by email
     │
     ├── check status
     │
     ├── verify password
     │
     └── generate JWT
     │
     ▼
AuthenticationResult
     │
     ├── User
     └── accessToken
```

Invalid credentials result in:

```text
InvalidCredentialsException
```

Suspended accounts result in:

```text
UserSuspendedException
```

A token is never generated if authentication fails.

---

# 15. JWT Architecture

The application uses JWT access tokens.

The JWT implementation is located in:

```text
infrastructure/security/
```

The application layer only depends on:

```text
TokenGenerator
```

The infrastructure implementation uses Spring Security's JWT support.

---

# 16. RSA Signing

JWTs are signed using an RSA key pair.

The architecture is:

```text
             RSA Key Pair
                 │
       ┌─────────┴─────────┐
       │                   │
Private Key            Public Key
       │                   │
       ▼                   ▼
 JwtEncoder            JwtDecoder
       │                   │
       ▼                   ▼
Create JWT            Verify JWT
```

The private key is used to sign tokens.

The public key is used to verify tokens.

This provides asymmetric signing and avoids sharing the signing secret with JWT consumers.

---

# 17. JWT Claims

The current access token contains:

```text
sub
email
role
iat
exp
```

Example conceptual payload:

```json
{
  "sub": "user-uuid",
  "email": "user@example.com",
  "role": "CUSTOMER",
  "iat": 1720000000,
  "exp": 1720003600
}
```

The `sub` claim identifies the authenticated user.

The `role` claim is converted into a Spring Security authority.

---

# 18. Role Conversion

The JWT contains:

```text
role = ADMIN
```

The security configuration converts it to:

```text
ROLE_ADMIN
```

This allows authorization rules such as:

```java
.hasRole("ADMIN")
```

The resulting flow is:

```text
JWT
 │
 │ role = ADMIN
 ▼
JwtAuthenticationConverter
 │
 │ ROLE_ADMIN
 ▼
Spring Security
 │
 ▼
Authorization decision
```

---

# 19. HTTP Security

The current security boundaries are:

### Public endpoints

```text
POST /api/auth/register
POST /api/auth/login

GET /api/products
GET /api/products/{id}

GET /actuator/health
```

### Protected Catalog operations

```text
POST   /api/products
PUT    /api/products/{id}
POST   /api/products/{id}/publish
POST   /api/products/{id}/assets
DELETE /api/products/{id}/assets/{assetId}
```

These require:

```text
ROLE_ADMIN
```

The application therefore distinguishes authentication from authorization:

```text
No valid JWT
     ↓
401 Unauthorized

Valid JWT + insufficient role
     ↓
403 Forbidden
```

---

# 20. Persistence

Identity uses PostgreSQL.

The database table is:

```text
users
```

Current schema:

```text
users
├── id
├── email
├── password_hash
├── role
├── status
├── created_at
└── updated_at
```

Email uniqueness is enforced by the database.

Flyway migration:

```text
V4__create_users_table.sql
```

---

# 21. Persistence Adapter

`UserPersistenceAdapter` implements the domain repository port:

```text
UserRepository
      ▲
      │
      │ implements
      │
UserPersistenceAdapter
      │
      ▼
UserJpaRepository
      │
      ▼
UserEntity
      │
      ▼
PostgreSQL
```

This keeps JPA-specific code outside the domain.

---

# 22. Domain ↔ Persistence Mapping

The domain model and persistence model are separate.

```text
Domain User
    │
    ▼
UserMapper
    │
    ▼
UserEntity
    │
    ▼
PostgreSQL
```

And when reading:

```text
PostgreSQL
    │
    ▼
UserEntity
    │
    ▼
UserMapper
    │
    ▼
Domain User
```

This prevents persistence annotations and database concerns from leaking into the domain model.

---

# 23. Web Layer

The web layer uses DTOs instead of exposing domain objects directly.

Registration:

```text
RegisterRequest
      ↓
RegisterUserUseCase
      ↓
User
      ↓
UserWebMapper
      ↓
UserResponse
```

Authentication:

```text
LoginRequest
      ↓
AuthenticateUserUseCase
      ↓
AuthenticationResult
      ↓
AuthResponse
```

Passwords and password hashes are never included in API responses.

---

# 24. Error Handling

Identity currently handles:

```text
UserAlreadyExistsException
InvalidCredentialsException
UserSuspendedException
```

The web exception handler translates application exceptions into HTTP responses.

The goal is to prevent internal implementation details from leaking through the REST API.

---

# 25. Testing Strategy

Identity is tested at multiple levels.

## Domain tests

Verify:

* User creation
* Email normalization
* Required fields
* User status transitions
* Role assignment

## Application tests

Registration tests verify:

* successful registration
* duplicate email rejection
* password hashing
* repository interaction
* password hash persistence behavior

Authentication tests verify:

* successful authentication
* correct password verification
* invalid password rejection
* unknown email rejection
* suspended-user rejection
* token generation only after successful authentication

## Persistence integration tests

Testcontainers PostgreSQL is used to verify:

* user persistence
* retrieval by ID
* retrieval by email
* email existence checks
* real PostgreSQL/Flyway compatibility

## Web integration tests

MockMvc tests verify:

* registration endpoint
* login endpoint
* request validation
* HTTP status codes
* response structure
* JWT authentication behavior

Catalog integration tests also generate real JWTs using the application's `JwtEncoder` to verify the complete security path.

---

# 26. Authentication Test Architecture

Protected Catalog integration tests use a real JWT generated through the application's `JwtEncoder`.

The test flow is:

```text
JwtEncoder
    │
    ▼
ADMIN JWT
    │
    │ Authorization: Bearer <token>
    ▼
Spring Security
    │
    ▼
JwtDecoder
    │
    ▼
role = ADMIN
    │
    ▼
ROLE_ADMIN
    │
    ▼
Catalog endpoint
```

This is preferable to simply mocking authentication because it verifies the actual JWT security configuration.

---

# 27. Dependency Direction

The Identity module follows these dependency rules:

```text
Web
 │
 ▼
Application
 │
 ▼
Domain

Infrastructure
 │
 ├── implements application ports
 └── implements domain repository ports
```

The domain must not depend on:

* Spring MVC
* Spring Data
* JPA
* PostgreSQL
* BCrypt
* JWT
* Stripe
* Redis
* S3

Infrastructure depends inward on the domain/application contracts.

---

# 28. Current Authentication Architecture

The complete current flow is:

```text
                    ┌─────────────────────┐
                    │      Client         │
                    └──────────┬──────────┘
                               │
                         POST /login
                               │
                               ▼
                    ┌─────────────────────┐
                    │   AuthController    │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ AuthenticateUser     │
                    │ Service              │
                    └──────┬───────┬──────┘
                           │       │
                find user  │       │ verify password
                           ▼       ▼
                    UserRepository
                           │
                           │
                    PasswordHasher
                           │
                           ▼
                    BCryptPasswordHasher
                           │
                           ▼
                    TokenGenerator
                           │
                           ▼
                    JwtTokenGenerator
                           │
                           ▼
                      JwtEncoder
                           │
                           ▼
                         JWT
```

For subsequent requests:

```text
Client
  │
  │ Authorization: Bearer JWT
  ▼
Spring Security
  │
  ▼
JwtDecoder
  │
  ▼
JWT signature validation
  │
  ▼
JWT claims
  │
  ▼
ROLE_CUSTOMER / ROLE_ADMIN
  │
  ▼
Authorization
  │
  ▼
Protected endpoint
```

---

# 29. Architectural Rules

The following rules should be preserved as the module evolves:

1. Raw passwords must never be persisted.
2. Password hashes must never be returned through REST responses.
3. Authentication logic belongs in the application layer.
4. BCrypt implementation belongs in infrastructure.
5. JWT implementation belongs in infrastructure.
6. Application code should depend on `PasswordHasher`, not BCrypt directly.
7. Application code should depend on `TokenGenerator`, not JWT classes directly.
8. Domain code must remain independent of Spring Security.
9. Persistence must remain behind `UserRepository`.
10. API DTOs must remain separate from domain models.
11. Authorization rules belong to the security infrastructure.
12. Protected endpoints must not be made public simply to bypass tests.
13. Database uniqueness constraints must remain in place even when application-level validation exists.

---

# 30. Current Limitations and Future Improvements

The current JWT key configuration generates an RSA key pair when the application starts.

This is suitable for local development, but production should use persistent secrets/keys managed through a secure mechanism.

Future improvements may include:

* persistent RSA key management
* refresh tokens
* token revocation/session management
* email verification
* password reset
* account deletion
* stronger password policies
* login rate limiting
* authentication audit logging
* Redis-backed session/token controls where appropriate
* more granular authorization
* administrative user management
* OAuth2/social login if required

These features should be introduced only when the corresponding business requirements exist.

---

# 31. Architectural Summary

Identity currently provides:

```text
                    Identity Module
                          │
        ┌─────────────────┼─────────────────┐
        │                 │                 │
      Domain         Application       Infrastructure
        │                 │                 │
        │                 │          ┌──────┴──────┐
        │                 │          │             │
      User          Use Cases     Persistence    Security
      UserId        Services      JPA Adapter    BCrypt
      Role          Ports         PostgreSQL     JWT
      Status        Exceptions
        │                 │
        └─────────────────┘
```

The module demonstrates the project's core architectural principles:

* Modular monolith
* Domain-driven design principles
* Hexagonal architecture
* Dependency inversion
* Separation of concerns
* Explicit application use cases
* Adapter-based infrastructure
* JWT-based authentication
* Role-based authorization
* Automated integration testing
* PostgreSQL persistence through Flyway
