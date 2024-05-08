export default defineNuxtPlugin(() => {
  const router = useRouter()
  const showingLoading = useState(useLoadingKey())
  const { boards, loaded } = storeToRefs(useBoardStore())
  const { list, sorter } = useBoardApi()

  router.beforeEach(async () => {
    showingLoading.value = false
    if (!loaded.value) {
      const { data, error } = await list()
      if (error.value) {
        useRouter().push('/erro?mensagem=Erro ao carregar os quadros de avisos.')
      } else {
        boards.value = data.value?.content || []
        boards.value.sort(sorter)
        loaded.value = true
      }
    }
  })
})
