import { defineStore } from 'pinia'

export interface TTokenStore {
  token: Ref<string | undefined>
  idToken: Ref<string | undefined>
  refreshToken: Ref<string | undefined>
  save: (newToken?: string, newIdToken?: string, newRefreshToken?: string) => void
  clear: () => void
}

export const tokenStoreName = 'tokenStore'

export const useTokenStore = defineStore<string, TTokenStore>(
  tokenStoreName,
  () => {
    const token = ref<string | undefined>(undefined)
    const idToken = ref<string | undefined>(undefined)
    const refreshToken = ref<string | undefined>(undefined)

    const save = (newToken?: string, newIdToken?: string, newRefreshToken?: string) => {
      token.value = newToken
      idToken.value = newIdToken
      refreshToken.value = newRefreshToken
    }

    const clear = () => {
      token.value = undefined
      idToken.value = undefined
      refreshToken.value = undefined
    }

    return { token, idToken, refreshToken, save, clear }
  },
  { persist: { storage: localStorage } }
)
