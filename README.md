# Docker Compose of Message Boards

### Frontend and Backend

# Configuration

## Requirements

- Docker Compose

## Installation

- Clone the repository
- Configure the .env file with the environment variables
- Run the command `docker compose up -d` in the project root

## Environment variables

- For frontend please refer to the README.md in the frontend folder
- For backend please refer to the README.md in the backend folder

- `SUBNET`: Subnet for the network. Default: 192.168.100.0/29
- `IP_BACKEND`: IP for the backend. Default: 192.168.100.3
- `IP_FRONTEND`: IP for the frontend. Default: 192.168.100.2

## Keycloak roles

- `messageboards_admin`: Admin role to use with some endpoints.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Authors

- [Thiago Suchorski](https://github.com/suchorski)

## Feel free to contribute
