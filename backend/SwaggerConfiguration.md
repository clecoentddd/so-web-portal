# Swagger/OpenAPI Configuration Guide

This document explains how to configure and use Swagger UI for testing your Spring Boot REST API.

## Overview

The project uses **SpringDoc OpenAPI 3** (version 2.0.3) which automatically generates OpenAPI documentation and provides an interactive Swagger UI for testing your REST endpoints.

## Dependencies

The following dependency is already configured in `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.3</version>
</dependency>
```

## Accessing Swagger UI

Once your application is running, you can access Swagger UI at:

**Swagger UI**: http://localhost:8080/swagger-ui.html

**OpenAPI JSON**: http://localhost:8080/v3/api-docs

**OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

## Configuration

### Basic Configuration (Optional)

You can customize Swagger behavior by adding properties to `application.yml`:

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    operationsSorter: method
    tagsSorter: alpha
  show-actuator: false
```

### Custom OpenAPI Configuration (Optional)

Create a configuration class to customize API documentation metadata:

**File**: `src/main/kotlin/boond/config/OpenApiConfig.kt`

```kotlin
package boond.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("SO Client Portal API")
                    .version("1.0.0")
                    .description("REST API for SO Client Portal - Spring Boot, Axon Framework, Kotlin")
                    .contact(
                        Contact()
                            .name("Your Team Name")
                            .email("team@example.com")
                    )
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                    )
            )
    }
}
```

## Using Swagger UI for Testing

### 1. Start Your Application

```bash
./mvnw spring-boot:run
```

Wait for the application to start (look for "Started Application in X seconds").

### 2. Open Swagger UI

Navigate to: http://localhost:8080/swagger-ui.html

### 3. Explore Endpoints

- **Controllers** are grouped by tags
- Click on any endpoint to expand it
- View request/response schemas
- See example values

### 4. Test an Endpoint

1. Click on an endpoint (e.g., `GET /api/users`)
2. Click **"Try it out"** button
3. Fill in any required parameters
4. Click **"Execute"**
5. View the response below (status code, body, headers)

### 5. Authentication (if configured)

If your API uses authentication:
1. Click the **"Authorize"** button at the top
2. Enter your credentials or token
3. Click **"Authorize"**
4. All subsequent requests will include authentication

## Annotating Your Controllers

To enhance Swagger documentation, use OpenAPI annotations in your controllers:

```kotlin
package boond.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management endpoints")
class UserController {

    @Operation(
        summary = "Get user by ID",
        description = "Retrieves a user by their unique identifier"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User found"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    @GetMapping("/{id}")
    fun getUserById(
        @Parameter(description = "User ID", required = true)
        @PathVariable id: String
    ): UserDto {
        // Implementation
    }
}
```

## Common Annotations

| Annotation | Purpose | Usage |
|------------|---------|-------|
| `@Tag` | Group endpoints | On controller class |
| `@Operation` | Describe endpoint | On controller method |
| `@Parameter` | Describe parameter | On method parameters |
| `@ApiResponse` | Document response | On controller method |
| `@Schema` | Describe model | On DTO classes/fields |
| `@Hidden` | Hide from docs | On any element |

## Example DTO with Schema Annotations

```kotlin
package boond.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "User data transfer object")
data class UserDto(
    @Schema(description = "Unique user identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: String,
    
    @Schema(description = "User's email address", example = "user@example.com", required = true)
    val email: String,
    
    @Schema(description = "User's full name", example = "John Doe")
    val name: String
)
```

## Troubleshooting

### Swagger UI Not Loading

1. Verify the application is running:
   ```bash
   curl http://localhost:8080/actuator/health
   ```

2. Check if SpringDoc is initialized in the logs:
   ```
   springdoc-openapi is running
   ```

3. Try the direct API docs URL:
   ```
   http://localhost:8080/v3/api-docs
   ```

### No Endpoints Showing

- Ensure your controllers are annotated with `@RestController`
- Check that controller methods have proper HTTP mapping annotations (`@GetMapping`, `@PostMapping`, etc.)
- Verify controllers are in a package scanned by Spring Boot

### Custom Port

If your application runs on a different port, check `application.yml`:

```yaml
server:
  port: 8080  # Change this if needed
```

Then access Swagger at: `http://localhost:<your-port>/swagger-ui.html`

## Quick Start Checklist

- [x] SpringDoc dependency added to `pom.xml`
- [ ] Application running (`./mvnw spring-boot:run`)
- [ ] Access Swagger UI at http://localhost:8080/swagger-ui.html
- [ ] Create REST controllers with proper annotations
- [ ] (Optional) Add OpenAPI configuration class
- [ ] (Optional) Enhance documentation with `@Operation`, `@Tag`, etc.

## Additional Resources

- [SpringDoc Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger UI Guide](https://swagger.io/tools/swagger-ui/)
