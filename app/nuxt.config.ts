export default defineNuxtConfig({
  devtools: { enabled: true },
  typescript: { shim: false },
  ssr: false,
  spaLoadingTemplate: false,
  app: {
    pageTransition: { name: 'fade', mode: 'out-in' },
  },
  css: ['quill/dist/quill.core.css', 'quill/dist/quill.snow.css'],
  modules: ['@nuxt/ui', '@pinia/nuxt', '@pinia-plugin-persistedstate/nuxt', '@formkit/auto-animate/nuxt'],
  ui: { global: true },
  colorMode: { preference: 'system' },
  runtimeConfig: {
    public: {
      PRODUCTION_MODE: process.env.NODE_ENV === 'production',
      LOGGER_LEVEL: process.env.LOGGER_LEVEL || 'warn',
      KEYCLOAK_URL: process.env.KEYCLOAK_URL,
      KEYCLOAK_REALM: process.env.KEYCLOAK_REALM,
      KEYCLOAK_CLIENT_ID: process.env.KEYCLOAK_CLIENT_ID,
      API_ENTRYPOINT: process.env.API_ENTRYPOINT,
    },
  },
})
