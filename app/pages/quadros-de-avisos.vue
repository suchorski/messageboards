<script lang="ts" setup>
import type { TBoard } from '~/composables/api/board'

const { list, sorter } = useBoardApi()
const { success, warning, danger } = useToaster()
const { show, hide } = useLoading()

const { updateBoard, removeBoard } = useBoardStore()

const { data: stateData, pending: statePending, error: stateError } = list()
watch(statePending, (pending) => {
  if (!pending && !stateError.value) {
    stateData.value?.content.sort(sorter)
  }
})

const askAdd = () => {
  const { prompt } = useModals()
  prompt('Novo Quadro de Avisos', 'Informe o nome do novo Quadro de Avisos:', async (name) => {
    const { add } = useBoardApi()
    show()
    const { data, error } = await add({ name })
    hide()
    if (error.value) {
      danger('Erro ao criar o Quadro de Avisos.', error.value?.data.message)
    } else {
      stateData.value?.content.push(data.value!)
      stateData.value?.content.sort(sorter)
      const { boards } = storeToRefs(useBoardStore())
      boards.value.push(data.value!)
      boards.value.sort(sorter)
      success('Quadro de Avisos criado com sucesso.')
    }
  })
}

const update = (board: TBoard) => {
  const index = stateData.value?.content.indexOf(board)
  if (index !== undefined) {
    updateBoard(board)
    success('Quadro de Avisos atualizado com sucesso.')
  } else {
    warning('Quadro de Avisos não encontrado.')
  }
}

const remove = (board: TBoard) => {
  const index = stateData.value?.content.indexOf(board)
  if (index !== undefined) {
    stateData.value?.content.splice(index, 1)
    removeBoard(board)
    success('Quadro de Avisos excluído com sucesso.')
  } else {
    warning('Quadro de Avisos não encontrado.')
  }
}
</script>

<template>
  <main>
    <header>
      <h1>Meus Quadros de Avisos</h1>
      <UButton :icon="useIcon().add" variant="soft" label="Novo Quadro de Avisos" @click="askAdd" />
    </header>
    <Transition name="fade" mode="out-in" appear>
      <section v-if="statePending" key="loading" class="loading">
        <SkeletonBoard v-for="i in 6" :key="i" class="w-full" />
      </section>
      <Alert
        v-else-if="stateError"
        key="error"
        error
        title="Erro ao obter os quadros"
        :description="stateError.data?.message"
      />
      <Alert v-else-if="!stateData?.content.length" key="empty" title="Nenhum quadro encontrado." warning />
      <section v-else key="data">
        <div class="boards" v-auto-animate>
          <Board v-for="board in stateData?.content" :key="board.id" :board="board" @update="update" @remove="remove" />
        </div>
      </section>
    </Transition>
  </main>
</template>

<style lang="postcss" scoped>
main {
  @apply p-2 space-y-4;
}

header {
  @apply flex flex-row justify-between items-center space-x-6;
}

section.loading {
  @apply flex flex-col space-y-4;
}

div.boards {
  @apply grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4;
}
</style>
