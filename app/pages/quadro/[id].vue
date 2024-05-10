<script lang="ts" setup>
import { type TMessage } from '~/composables/api/message'

const { boards } = storeToRefs(useBoardStore())

const { success, warning } = useToaster()

const {
  listByBoardId,
  sorter,
  sorterDesc,
  creationDateSorter,
  creationDateSorterDesc,
  deadlineDateSorter,
  deadlineDateSorterDesc,
} = useMessageApi()

const { toDate } = useDate()

const boardId: number = Number(useRoute().params.id)
const board = computed(() => boards.value.find((b) => b.id === boardId))

const accordion = [{ label: 'Adicionar Novo Aviso', icon: useIcon().add }]
const editor = ref<string>('')
const deadlineEnabled = ref<boolean>(false)
const deadline = ref<Date>(new Date())

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
    const { data, error } = await add({
      text: editor.value,
      deadline: deadlineEnabled.value ? deadline.value : null,
      board: { id: boardId },
    })
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

const asc = ref<boolean>(true)
const sortByLastUpdate = () => {
  asc.value = !asc.value
  stateData.value?.sort(asc.value ? sorter : sorterDesc)
}
const sortByCreationDate = () => {
  asc.value = !asc.value
  stateData.value?.sort(asc.value ? creationDateSorter : creationDateSorterDesc)
}
const sortByDeadline = () => {
  asc.value = !asc.value
  stateData.value?.sort(asc.value ? deadlineDateSorter : deadlineDateSorterDesc)
}
</script>

<template>
  <main>
    <h1>Quadro de Avisos: {{ board!.name }}</h1>
    <UAccordion :items="accordion">
      <template #item>
        <div class="add">
          <Editor v-model="editor" />
          <div class="deadline">
            <div class="enabled">
              <h3>Definir uma data de prazo?</h3>
              <UToggle color="primary" v-model="deadlineEnabled" />
            </div>
            <div class="flex-">&nbsp;</div>
            <Transition name="fade" mode="out-in">
              <div class="datepicker" v-if="deadlineEnabled" key="deadline">
                <h3>Prazo:</h3>
                <UPopover :popper="{ placement: 'bottom-end' }">
                  <UButton :icon="useIcon().calendar" :label="toDate(deadline)" class="px-16" />
                  <template #panel="{ close }">
                    <DatePicker v-model="deadline" @close="close" />
                  </template>
                </UPopover>
              </div>
            </Transition>
          </div>
          <UButton @click="askAdd" block :disabled="editor.trim().length === 0">Salvar</UButton>
        </div>
      </template>
    </UAccordion>
    <div class="sorter">
      <h2>Ordenar por:</h2>
      <UButtonGroup class="buttons">
        <UButton :icon="asc ? useIcon().down : useIcon().up" :disabled="statePending" block @click="sortByLastUpdate"
          >Última Atualização</UButton
        >
        <UButton :icon="asc ? useIcon().down : useIcon().up" :disabled="statePending" block @click="sortByCreationDate"
          >Data de Criação</UButton
        >
        <UButton :icon="asc ? useIcon().down : useIcon().up" :disabled="statePending" block @click="sortByDeadline"
          >Prazo</UButton
        >
      </UButtonGroup>
    </div>
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

div.add > div.deadline {
  @apply h-8 flex flex-row justify-between items-center space-x-2;
}

div.add > div.deadline > div {
  @apply flex flex-row items-center space-x-2;
}

div.sorter {
  @apply pt-1 pb-3 flex flex-col space-y-2;
}

div.sorter > .buttons {
  @apply flex flex-row;
}

div.sorter > .buttons > button {
  @apply flex-1;
}
</style>
