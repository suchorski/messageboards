export default defineNuxtPlugin(() => {
  const config = useRuntimeConfig().public
  const router = useRouter()
  const showingLoading = useState(useLoadingKey())
  const { boards, loaded } = storeToRefs(useBoardStore())
  const { list, sorter } = useBoardApi()

  router.beforeEach(async () => {
    showingLoading.value = false
    if (!loaded.value) {
      const { data, error } = await list()
      loaded.value = true
      if (error.value?.message.toLowerCase().match(/.+?:\s\<no response\>.+?/)) {
        return (window.location.href = config.API_ENTRYPOINT)
      }
      if (error.value) {
        const message = error.value?.message || 'Erro ao carregar os quadros de avisos.'
        return router.push(`/erro?mensagem=${message}`)
      }
      boards.value = data.value?.content || []
      boards.value.sort(sorter)
    }
  })
})
