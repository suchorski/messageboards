# Backend of Message Boards APP

## Configuration

### Requirements

- Java 21+
- Maven 3.9+

### Installation

- Clone the repository
- Run the command `mvn clean package` in the project root
- Run the command `java -jar target/dadosmilitar-backend.jar` in the project root

### Environment variables

- `JDBC_DATABASE_URL`: JDBC URL for database connection.
- `JDBC_DATABASE_USERNAME`: Database username.
- `JDBC_DATABASE_PASSWORD`: Database password.
- `OAUTH2_ISSUER_URI`: Keycloak authentication server URL.
- `CORS_ALLOWED_ORIGINS`: Allowed origins for CORS.
- `APP_PROFILE`: Application execution profile. Default: `production`
- `LOGGING_LEVEL`: Log level. Default: `INFO`

### Keycloak roles

- `messageboards_admin`: Admin role to use with some endpoints.

## Authors

- [Thiago Suchorski](https://github.com/suchorski)

## Feel free to contribute
