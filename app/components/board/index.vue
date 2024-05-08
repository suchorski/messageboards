<script lang="ts" setup>
import { type TBoard } from '~/composables/api/board'
import { type TAllocation } from '~/composables/api/allocation'

const props = defineProps<{ board: TBoard }>()
const emit = defineEmits<{
  (e: 'update', value: TBoard): void
  (e: 'remove', value: TBoard): void
}>()

const { success, warning, danger } = useToaster()
const { show, hide } = useLoading()

const isAllocationsOpen = ref(false)
const isAllocationsPending = ref(true)
const allocations = ref<TAllocation[]>([])

const askAllocate = (id: number) => {
  const { prompt } = useModals()
  prompt('Nova Alocação', 'Informe o CPF do militar:', async (cpf) => {
    const { add } = useAllocationApi()
    show()
    const { error } = await add({ cpf, board: { id }, administrator: false, moderator: false })
    hide()
    if (error.value) {
      danger('Erro ao alocar o militar.', error.value?.data.message)
    } else {
      success('Militar alocado com sucesso.')
    }
  })
}

const askEdit = () => {
  const { prompt } = useModals()
  prompt('Editar Quadro de Avisos', 'Informe o novo nome do Quadro de Avisos:', async (name) => {
    const { updateName } = useBoardApi()
    show()
    const { error } = await updateName(props.board, name)
    hide()
    if (error.value) {
      danger('Erro ao editar o Quadro de Avisos.', error.value?.data.message)
    } else {
      props.board.name = name
      emit('update', { ...props.board, name })
    }
  })
}

const askRemove = () => {
  const { confirm } = useModals()
  confirm('Confirmação de Exclusão', 'Deseja realmente excluir o Quadro de Avisos?', async () => {
    const { remove } = useBoardApi()
    show()
    const { error } = await remove(props.board)
    hide()
    if (error.value) {
      danger('Erro ao excluir o Quadro de Avisos.', error.value?.data.message)
    } else {
      emit('remove', props.board)
    }
  })
}

const openAllocations = async () => {
  isAllocationsPending.value = true
  isAllocationsOpen.value = true
  const { listByBoardId, sorter } = useAllocationApi()
  const { data, error } = await listByBoardId(props.board.id!)
  isAllocationsPending.value = false
  if (error.value) {
    isAllocationsOpen.value = false
    danger('Erro ao carregar alocações.', error.value?.data.message)
  } else {
    data.value?.sort(sorter)
    allocations.value = data.value || []
  }
}

const removeAllocation = (allocation: TAllocation) => {
  const index = allocations.value.indexOf(allocation)
  if (index !== undefined) {
    allocations.value.splice(index, 1)
    success('Alocação excluída com sucesso.')
  } else {
    warning('Alocação não encontrada.')
  }
}
</script>

<template>
  <section class="board">
    <UCard>
      <h2>{{ board.name }}</h2>
      <template #footer>
        <section class="actions">
          <UTooltip text="Usuários alocados no Quadro de Avisos">
            <UButton
              :icon="useIcon().users"
              block
              aria-label="Usuários alocados"
              class="no-text"
              @click="openAllocations"
            />
            <UButton :icon="useIcon().users" block class="text" @click="openAllocations">Usuários Alocados</UButton>
          </UTooltip>
          <UTooltip text="Alocar Usuário ao Quadro de Avisos">
            <UButton
              :icon="useIcon().add"
              color="green"
              block
              aria-label="Alocar Usuário"
              class="no-text"
              @click="askAllocate(board.id!)"
            />
            <UButton :icon="useIcon().add" color="green" block class="text" @click="askAllocate(board.id!)"
              >Alocar Usuário</UButton
            >
          </UTooltip>
          <UTooltip text="Editar nome do Quadro de Avisos">
            <UButton
              :icon="useIcon().pencil"
              color="orange"
              block
              aria-label="Editar"
              class="no-text"
              @click="askEdit"
            />
            <UButton :icon="useIcon().pencil" color="orange" block class="text" @click="askEdit">Editar</UButton>
          </UTooltip>
          <UTooltip text="Excluir Quadro de Avisos">
            <UButton
              :icon="useIcon().trash"
              color="red"
              block
              aria-label="Excluir"
              class="no-text"
              @click="askRemove"
            />
            <UButton :icon="useIcon().trash" color="red" block class="text" @click="askRemove">Excluir</UButton>
          </UTooltip>
        </section>
      </template>
    </UCard>
    <UModal v-model="isAllocationsOpen">
      <UCard>
        <template #header>
          <header class="allocations">
            <Transition name="fade" mode="out-in">
              <h2 v-if="isAllocationsPending">Carregando alocações...</h2>
              <h2 v-else>{{ board.name }}</h2>
            </Transition>
            <UButton :icon="useIcon().close" variant="soft" aria-label="Fechar" @click="isAllocationsOpen = false" />
          </header>
          <UDivider class="my-2" />
          <section class="allocations">
            <Transition name="fade" mode="out-in">
              <section v-if="isAllocationsPending" key="loading" class="loading">
                <SkeletonAllocation v-for="i in 3" :key="i" class="w-full" />
              </section>
              <ul v-else key="data" class="data" v-auto-animate>
                <BoardAllocation v-for="a in allocations" :key="a.id!" :allocation="a" @remove="removeAllocation" />
              </ul>
            </Transition>
          </section>
        </template>
      </UCard>
    </UModal>
  </section>
</template>

<style lang="postcss" scoped>
.actions {
  @apply flex flex-row justify-between items-center space-x-2;
}

.actions > * {
  @apply flex-1;
}

.no-text {
  @apply 2xl:hidden;
}

.text {
  @apply hidden 2xl:flex;
}

header.allocations {
  @apply flex flex-row justify-between items-center;
}

section.allocations > section.loading {
  @apply flex flex-col space-y-2;
}

section.allocations > ul {
  @apply flex flex-col space-y-2;
}
</style>
