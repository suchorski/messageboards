# Frontend of Message Boards APP

## Configuration

### Requirements

- node 20+
- pnpm 9+

### Development

- Clone the repository
- Install the dependencies with `pnpm install`
- Run the project with `pnpm dev`

### Production

- Clonar o repositório
- Instalar as dependências com `yarn install`
- Construir o projeto com `yarn build`
- Executar o projeto com `node .output/server/index.mjs`

### Environment Variables

Create a `.env` file in the root of the project with the following content:

- `KEYCLOAK_URL`: URL of the configured Keycloak server
- `KEYCLOAK_REALM`: Realm configured in the Keycloak server
- `KEYCLOAK_CLIENT_ID`: Client ID configured in the Keycloak server
- `API_ENTRYPOINT`: URL of the backend server
- `DEFAULT_DEADLINE_HOURS`: Default deadline for new messages in hours. Default is 16 (4pm).
- `DEFAULT_DEADLINE_MINUTES`: Default deadline for new messages in minutes. Default is 0.

## Authors

- [Thiago Suchorski](https://github.com/suchorski)

## Feel free to contribute
