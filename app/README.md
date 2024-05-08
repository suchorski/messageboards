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

- Clone the repository
- Install the dependencies with `pnpm install`
- Build the project with `pnpm build`
- Run the project with `node .output/server/index.mjs`

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

## Authors

- [Thiago Suchorski](https://github.com/suchorski)

## Feel free to contribute