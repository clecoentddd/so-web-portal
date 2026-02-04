# PostgreSQL Explorer configuration Guide

This guide explains how to connect the **PostgreSQL Explorer** (or similar) extension in your editor to the project's database.

## Connection Details

Based on your `docker-compose.yml` and `application.yml`, use these credentials:

| Field | Value |
| :--- | :--- |
| **Host** | `localhost` |
| **Port** | `5432` |
| **User** | `postgres` |
| **Password** | `postgres` |
| **Database** | `postgres-so-portal` |
| **SSL** | Disable (uncheck/off) |

## Steps to Add Connection (VS Code)

1. **Install Extension**: Ensure you have the "PostgreSQL" or "PostgreSQL Explorer" extension installed.
2. **Open Explorer**: Click on the PostgreSQL icon in the Activity Bar (usually on the left).
3. **Add Connection**: Click the **+** (plus icon) or "Add Connection".
4. **Enter Host**: Type `localhost` and press Enter.
5. **Enter User**: Type `postgres` and press Enter.
6. **Enter Password**: Type `postgres` and press Enter.
7. **Enter Port**: Type `5432` and press Enter.
8. **SSL Configuration**: Choose `Use Color` or `Secure Connection` -> **No** (standard for local docker).
9. **Enter Database**: Type `postgres-so-portal` and press Enter.
10. **Display Name**: (Optional) Type `SO Portal DB` and press Enter.

## Verifying the Connection

- Make sure your Docker container is running: `docker-compose up -d`.
- In the explorer, expand the connection. You should see:
    - **Databases** > `postgres-so-portal`
    - **Schemas** > `public`
    - **Tables** (This will contain the tables created by Flyway once the app runs).

## Troubleshooting

- **Connection Refused**: Ensure the Docker container is running (`docker ps`).
- **Authentication Failed**: Verify the password is exactly `postgres`.
- **Database Not Found**: If the database was just created, try right-clicking the connection and selecting **Refresh**.
