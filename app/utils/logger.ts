const getLevel = (level: string) => {
  switch (level.toLowerCase()) {
    case 'all':
      return 0
    case 'fine':
      return 10
    case 'info':
      return 20
    case 'warn':
      return 30
    case 'error':
      return 40
    default:
      return 50
  }
}

export const useLogger = () => {
  const config = useRuntimeConfig().public
  const isDev = !config.PRODUCTION_MODE
  const level = getLevel(config.LOGGER_LEVEL)

  const fine = (...args: unknown[]) => {
    if (isDev || getLevel('fine') >= level) {
      console.log.apply(console.log, args)
    }
  }

  const info = (...args: unknown[]) => {
    if (isDev || getLevel('info') >= level) {
      console.info.apply(console.info, args)
    }
  }

  const warn = (...args: unknown[]) => {
    if (isDev || getLevel('warn') >= level) {
      console.warn.apply(console.warn, args)
    }
  }

  const error = (...args: unknown[]) => {
    if (isDev || getLevel('error') >= level) {
      console.error.apply(console.error, args)
    }
  }

  return { fine, info, warn, error }
}
