import { defineStore } from 'pinia'
import Keycloak from 'keycloak-js'

export interface TAuthStore {
  loggedIn: Ref<boolean | undefined>
  adapter: Ref<Keycloak | undefined>
  userInfo: Ref<TAuthStoreUserInfo | undefined>
  initialized: (adapter: Keycloak) => void
  authenticated: () => boolean
  updateUserInfo: (adapter: Keycloak) => void
  logout: () => void
}

export interface TAuthStoreUserInfo {
  firstName: string
  lastName: string
  email: string
  isAdmin: boolean
}

export const authStoreName = 'authStore'

export const useAuthStore = defineStore<string, TAuthStore>(
  authStoreName,
  () => {
    const loggedIn = ref<boolean>()
    const adapter = ref<Keycloak>()
    const userInfo = ref<TAuthStoreUserInfo>()

    const initialized = (payload: Keycloak) => {
      adapter.value = payload
      updateUserInfo(payload)
      loggedIn.value = adapter.value?.authenticated || false
    }
    const authenticated = () => loggedIn.value || false
    const updateUserInfo = (payload: Keycloak) => {
      userInfo.value = {
        firstName: payload.tokenParsed?.given_name as string,
        lastName: payload.tokenParsed?.family_name as string,
        email: payload.tokenParsed?.email as string,
        isAdmin: payload.hasRealmRole('admin'),
      }
    }
    const logout = () => adapter.value?.logout()

    return { loggedIn, adapter, userInfo, initialized, authenticated, updateUserInfo, logout }
  },
  { persist: { storage: localStorage } }
)
