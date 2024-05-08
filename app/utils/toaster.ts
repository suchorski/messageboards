export const useToaster = () => {
  const toast = useToast()

  const id = () => `global:${Math.random()}:${Math.random()}`

  const notify = (title: string, description?: string, timeout: number = 3000) => {
    toast.add({ id: id(), title, description, timeout })
  }

  const success = (title: string, description?: string, timeout: number = 3000) => {
    toast.add({ id: id(), title, description, timeout, color: 'green' })
  }

  const warning = (title: string, description?: string, timeout: number = 3000) => {
    toast.add({ id: id(), title, description, timeout, color: 'yellow' })
  }

  const danger = (title: string, description?: string, timeout: number = 5000) => {
    toast.add({ id: id(), title, description, timeout, color: 'red' })
  }

  return { notify, success, warning, danger }
}
