<script lang="ts" setup>
import type { DatePickerDate } from 'v-calendar/dist/types/src/use/datePicker.js'
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

const { toDateString } = useDate()

const boardId: number = Number(useRoute().params.id)
const board = computed(() => boards.value.find((b) => b.id === boardId))

const accordion = [{ label: 'Adicionar Novo Aviso', icon: useIcon().add }]

const { text, deadlined, deadline } = storeToRefs(useMessageStore())

const { data: stateData, pending: statePending, error: stateError } = listByBoardId(boardId)
watch(stateData, (newValue, oldValue) => {
  if (newValue && newValue !== oldValue && stateError.value === null) {
    stateData.value?.sort(deadlineDateSorter)
  }
})

const askAdd = () => {
  const { confirm } = useModals()
  confirm('Adicionar Novo Aviso', 'Deseja realmente adicionar um novo Aviso?', async () => {
    const { add } = useMessageApi()
    const { data, error } = await add({
      text: text.value,
      deadline: deadline.value.toISOString() ?? null,
      board: { id: boardId },
    })
    if (error.value) {
      warning('Erro ao adicionar o Aviso.', error.value?.data.message)
    } else {
      success('Aviso adicionado com sucesso.')
      stateData.value?.unshift(data.value!)
      text.value = ''
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
const selected = ref<'lastUpdate' | 'creationDate' | 'deadline'>('deadline')
const sortByLastUpdate = () => {
  asc.value = !asc.value
  selected.value = 'lastUpdate'
  stateData.value?.sort(asc.value ? sorter : sorterDesc)
}
const sortByCreationDate = () => {
  asc.value = !asc.value
  selected.value = 'creationDate'
  stateData.value?.sort(asc.value ? creationDateSorter : creationDateSorterDesc)
}
const sortByDeadline = () => {
  asc.value = !asc.value
  selected.value = 'deadline'
  stateData.value?.sort(asc.value ? deadlineDateSorter : deadlineDateSorterDesc)
}

const clearMessage = () => {
  text.value = ''
  deadlined.value = false
  deadline.value = new Date()
}

onMounted(() => {
  const { public: config } = useRuntimeConfig()
  deadline.value = new Date()
  deadline.value.setHours(Number(config.DEFAULT_DEADLINE_HOURS), Number(config.DEFAULT_DEADLINE_MINUTES), 0, 0)
})
</script>

<template>
  <main>
    <h1>Quadro de Avisos: {{ board!.name }}</h1>
    <UAccordion :items="accordion">
      <template #item>
        <div class="add">
          <Editor v-model="text" />
          <div class="deadline">
            <div class="enabled">
              <UButton label="Limpar Aviso" color="red" @click="clearMessage" />
              <h3>Definir uma data de prazo?</h3>
              <UToggle color="primary" v-model="deadlined" />
            </div>
            <div class="flex-">&nbsp;</div>
            <Transition name="fade" mode="out-in">
              <div class="datepicker" v-if="deadlined" key="deadline">
                <h3>Prazo:</h3>
                <UPopover :popper="{ placement: 'bottom-end' }">
                  <UButton :icon="useIcon().calendar" :label="toDateString(deadline)" class="px-16" />
                  <template #panel="{ close }">
                    <DatePicker v-model="deadline" @close="close" />
                  </template>
                </UPopover>
              </div>
            </Transition>
          </div>
          <UButton @click="askAdd" block :disabled="text.trim().length === 0">Salvar</UButton>
        </div>
      </template>
    </UAccordion>
    <div class="sorter">
      <h2>Ordenar por:</h2>
      <UButtonGroup class="buttons">
        <UButton :disabled="statePending" block @click="sortByLastUpdate">
          <template #trailing>
            <Transition name="rotate" mode="out-in">
              <UIcon v-if="selected !== 'lastUpdate'" key="none" :name="useIcon().right" />
              <UIcon v-else-if="asc" key="asc" :name="useIcon().down" />
              <UIcon v-else key="desc" :name="useIcon().up" />
            </Transition>
          </template>
          Última Atualização
        </UButton>
        <UButton :disabled="statePending" block @click="sortByCreationDate">
          <template #trailing>
            <Transition name="rotate" mode="out-in">
              <UIcon v-if="selected !== 'creationDate'" key="none" :name="useIcon().right" />
              <UIcon v-else-if="asc" key="asc" :name="useIcon().down" />
              <UIcon v-else key="desc" :name="useIcon().up" />
            </Transition>
          </template>
          Data de Criação
        </UButton>
        <UButton :disabled="statePending" block @click="sortByDeadline">
          <template #trailing>
            <Transition name="rotate" mode="out-in">
              <UIcon v-if="selected !== 'deadline'" key="none" :name="useIcon().right" />
              <UIcon v-else-if="asc" key="asc" :name="useIcon().down" />
              <UIcon v-else key="desc" :name="useIcon().up" />
            </Transition>
          </template>
          Prazo
        </UButton>
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

button > span {
  @apply size-4;
}
</style>
