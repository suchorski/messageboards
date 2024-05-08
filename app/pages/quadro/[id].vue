<script lang="ts" setup>
import { type TMessage } from '~/composables/api/message'

const { boards } = storeToRefs(useBoardStore())

const { success, warning } = useToaster()

const { listByBoardId, sorter } = useMessageApi()

const boardId: number = Number(useRoute().params.id)
const board = computed(() => boards.value.find((b) => b.id === boardId))

const accordion = [{ label: 'Adicionar Novo Aviso', icon: useIcon().add }]
const editor = ref<string>('')

const { data: stateData, pending: statePending, error: stateError } = listByBoardId(boardId)
watch(stateData, (newValue, oldValue) => {
  if (newValue && newValue !== oldValue && stateError.value === null) {
    stateData.value?.sort(sorter)
  }
})  

const askAdd = () => {
  const { confirm } = useModals()
  confirm('Adicionar Novo Aviso', 'Deseja realmente adicionar um novo Aviso?', async () => {
    const { add } = useMessageApi()
    const { data, error } = await add({ text: editor.value, board: { id: boardId } })
    if (error.value) {
      warning('Erro ao adicionar o Aviso.', error.value?.data.message)
    } else {
      success('Aviso adicionado com sucesso.')
      stateData.value?.unshift(data.value!)
      editor.value = ''
    }
  })
}

const finalize = (message: TMessage) => {
  const index = stateData.value?.indexOf(message)
  if (index !== undefined) {
    stateData.value?.splice(index, 1)
    success('Aviso finalizado com sucesso.')
  } else {
    warning('Aviso não encontrado.')
  }
}

const remove = (message: TMessage) => {
  const index = stateData.value?.indexOf(message)
  if (index !== undefined) {
    stateData.value?.splice(index, 1)
    success('Aviso excluído com sucesso.')
  } else {
    warning('Aviso não encontrado.')
  }
}
</script>

<template>
  <main>
    <h1>Quadro de Avisos: {{ board!.name }}</h1>
    <UAccordion :items="accordion">
      <template #item>
        <div class="add">
          <Editor v-model="editor" />
          <UButton @click="askAdd" block :disabled="editor.trim().length === 0">Salvar</UButton>
        </div>
      </template>
    </UAccordion>
    <Transition name="fade" mode="out-in">
      <section v-if="statePending" key="loading" class="loading">
        <SkeletonMessage v-for="i in 4" :key="i" />
      </section>
      <Alert
        v-else-if="stateError !== null"
        key="error"
        title="Erro ao carregar os avisos"
        :description="stateError.data.message || 'Erro desconhecido.'"
        error
      />
      <Alert v-else-if="(stateData?.length || 0) === 0" key="empty" title="Nenhum aviso cadastrado." warning />
      <section v-else key="data" class="data" v-auto-animate>
        <Message v-for="m in stateData" :key="m.id!" :message="m" @finalize="finalize" @remove="remove" />
      </section>
    </Transition>
  </main>
</template>

<style lang="postcss" scoped>
main {
  @apply p-2 flex flex-col space-y-2;
}

section.loading {
  @apply flex flex-col space-y-4;
}

section.data {
  @apply flex flex-col space-y-2;
}

div.add {
  @apply flex flex-col space-y-2;
}
</style>
