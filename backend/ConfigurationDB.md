# Database Configuration Guide

This document explains how to set up and configure the PostgreSQL database for the SO Client Portal backend application.

## Prerequisites

- Docker and Docker Compose installed
- Maven Wrapper configured (see project setup)

## Database Setup

### 1. Docker Compose Configuration

The project uses PostgreSQL 16 running in a Docker container. The configuration is defined in `docker-compose.yml`:

```yaml
services:
  postgres:
    image: postgres:16
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: postgres-so-portal
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

### 2. Starting the Database

To start the PostgreSQL database, run:

```bash
docker-compose up -d
```

This command will:
- Download the PostgreSQL 16 image (if not already present)
- Create a container named `backend-postgres-1`
- Automatically create the database `postgres-so-portal`
- Persist data in a Docker volume named `backend_postgres_data`

### 3. Verify Database Creation

To verify the database was created successfully:

```bash
docker exec backend-postgres-1 psql -U postgres -l
```

You should see `postgres-so-portal` in the list of databases.

### 4. Recreating the Database (Clean Start)

If you need to start fresh and recreate the database from scratch:

```bash
# Stop and remove containers, networks, and volumes
docker-compose down -v

# Start fresh
docker-compose up -d
```

**Warning:** The `-v` flag will delete all data in the database!

## Application Configuration

The database connection is configured in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres-so-portal
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver

  flyway:
    url: jdbc:postgresql://localhost:5432/postgres-so-portal
    locations: classpath:db/migration
    user: postgres
    password: postgres
```

## Database Migrations

This project uses Flyway for database migrations. Migration scripts should be placed in:

```
src/main/resources/db/migration/
```

Flyway will automatically run migrations when the application starts.

## Troubleshooting

### Database Does Not Exist Error

If you see an error like:
```
FATAL: database "postgres-so-portal" does not exist
```

**Solution:** Recreate the Docker container:
```bash
docker-compose down -v
docker-compose up -d
```

### Connection Refused Error

If the application cannot connect to the database:

1. Verify the container is running:
   ```bash
   docker ps
   ```

2. Check PostgreSQL logs:
   ```bash
   docker logs backend-postgres-1
   ```

3. Ensure port 5432 is not already in use by another process

### Manual Database Creation

If the database wasn't created automatically, you can create it manually:

```bash
docker exec -it backend-postgres-1 psql -U postgres -c "CREATE DATABASE \"postgres-so-portal\";"
```

## Running the Application

Once the database is running, start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The application will:
1. Connect to the PostgreSQL database
2. Run Flyway migrations
3. Start the embedded Tomcat server
