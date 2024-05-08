export default defineAppConfig({
  ui: {
    primary: 'sky',
    gray: 'cool',
    strategy: 'override',
    container: { padding: 'p-0', constrained: 'max-w-full' },
    skeleton: { background: 'bg-primary-200 dark:bg-primary-800' },
    tooltip: { popper: { arrow: true } },
  },
})
