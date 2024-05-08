import type { Config } from 'tailwindcss'

export default <Partial<Config>>{
  content: ['app.vue', 'components/**/*.vue', 'layouts/**/*.vue', 'pages/**/*.vue'],
}
