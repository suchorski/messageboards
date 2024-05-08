import Keycloak, { type KeycloakInitOptions } from 'keycloak-js'
import { useTokenStore, useAuthStore } from '#imports'

export default defineNuxtPlugin(async ({ $config }) => {
  const { fine, error } = useLogger()
  const tokenStore = useTokenStore()
  const authStore = useAuthStore()
  const { token, idToken, refreshToken } = storeToRefs(tokenStore)
  const { loggedIn } = storeToRefs(authStore)
  const { KEYCLOAK_URL, KEYCLOAK_REALM, KEYCLOAK_CLIENT_ID, PRODUCTION_MODE } = $config.public

  const initOptions: KeycloakInitOptions = {
    onLoad: 'login-required',
    checkLoginIframe: false,
    token: token.value,
    idToken: idToken.value,
    refreshToken: refreshToken.value,
    enableLogging: !PRODUCTION_MODE,
  }

  const adapter: Keycloak = new Keycloak({
    url: KEYCLOAK_URL,
    realm: KEYCLOAK_REALM,
    clientId: KEYCLOAK_CLIENT_ID,
  })

  adapter.onAuthSuccess = () => {
    fine('onAuthSuccess called')
    tokenStore.save(adapter.token, adapter.idToken, adapter.refreshToken)
    authStore.updateUserInfo(adapter)
    loggedIn.value = true
  }
  adapter.onAuthRefreshSuccess = () => {
    fine('onAuthRefreshSuccess called')
    tokenStore.save(adapter.token, adapter.idToken, adapter.refreshToken)
    authStore.updateUserInfo(adapter)
    loggedIn.value = true
  }
  adapter.onAuthError = () => {
    fine('onAuthError called')
    loggedIn.value = false
    tokenStore.clear()
  }
  adapter.onAuthRefreshError = () => {
    fine('onAuthRefreshError called')
    loggedIn.value = false
    tokenStore.clear()
  }
  adapter.onAuthLogout = () => {
    fine('onAuthLogout called')
    loggedIn.value = false
    tokenStore.clear()
  }
  adapter.onTokenExpired = () => {
    fine('onTokenExpired called')
    adapter
      .updateToken(10)
      .then((refreshed) => {
        if (refreshed) {
          fine('Token refreshed')
          tokenStore.save(adapter.token, adapter.idToken, adapter.refreshToken)
          authStore.updateUserInfo(adapter)
          loggedIn.value = true
        } else {
          fine('Token still valid')
        }
      })
      .catch(() => {
        fine('Token expired')
        loggedIn.value = false
        tokenStore.clear()
      })
  }

  await adapter
    .init(initOptions)
    .catch((err) => error(err))
    .finally(() => fine('Keycloak initialized'))

  useAuthStore().initialized(adapter)
})
