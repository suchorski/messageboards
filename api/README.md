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
- `LDAP_ENABLED`: Enable LDAP authentication. Default: `false`
- `LDAP_PROVIDER_URL`: LDAP provider URL.
- `LDAP_SECURITY_AUTHENTICATION`: LDAP security authentication type. Usualy `simple`.
- `LDAP_SECURITY_PRINCIPAL`: LDAP security principal.
- `LDAP_SECURITY_CREDENTIALS`: LDAP security credentials.
- `LDAP_PARAMETERS_NAME`: LDAP parameters name.
- `LDAP_PARAMETERS_NICKNAME`: LDAP parameters nickname.
- `LDAP_PARAMETERS_RANK`: LDAP parameters rank.
- `LDAP_PARAMETERS_COMPANY`: LDAP parameters company.
- `LDAP_FILTERS_BASE_OU`: LDAP filters base OU.
- `LDAP_FILTERS_USER_FILTER`: LDAP filters user filter. Usualy `(uid=%s)`

### Keycloak roles

- `messageboards_admin`: Admin role to use with some endpoints.

## Authors

- [Thiago Suchorski](https://github.com/suchorski)

## Feel free to contribute
