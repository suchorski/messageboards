export const useLoadingKey = (): 'loadingKey' => 'loadingKey'

export const useLoading = () => {
  const showingLoading = useState(useLoadingKey())

  const show = () => {
    showingLoading.value = true
  }

  const hide = () => {
    showingLoading.value = false
  }

  return { show, hide }
}
