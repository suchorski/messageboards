<script lang="ts" setup>
import { type TAllocation } from '~/composables/api/allocation'

const props = defineProps<{ allocation: TAllocation }>()
const emit = defineEmits<{ (e: 'remove', value: TAllocation): void }>()

const { update } = useAllocationApi()
const { success, danger } = useToaster()
const { show, hide } = useLoading()

const isModerator = ref(props.allocation.moderator)
const isAdministrator = ref(props.allocation.administrator)
const undoAllocationUpdate = ref(false)

const updateAllocation = async (administrator: boolean) => {
  show()
  const { error } = await update(props.allocation.id!, isModerator.value, isAdministrator.value)
  hide()
  if (error.value) {
    undoAllocationUpdate.value = true
    if (administrator) {
      isAdministrator.value = !isAdministrator.value
    } else {
      isModerator.value = !isModerator.value
    }
    danger('Erro ao atualizar alocação.', error.value?.data.message)
  } else {
    success('Alocação atualizada com sucesso.')
  }
}
watch(isModerator, () => {
  if (!undoAllocationUpdate.value) updateAllocation(false)
  undoAllocationUpdate.value = false
})
watch(isAdministrator, () => {
  if (!undoAllocationUpdate.value) updateAllocation(true)
  undoAllocationUpdate.value = false
})

const askRemove = () => {
  const { confirm } = useModals()
  confirm('Confirmação de Exclusão', 'Deseja realmente excluir a alocação?', async () => {
    const { remove } = useAllocationApi()
    show()
    const { error } = await remove(props.allocation)
    hide()
    if (error.value) {
      danger('Erro ao excluir a alocação.', error.value?.data.message)
    } else {
      emit('remove', props.allocation)
    }
  })
}
</script>

<template>
  <li>
    <section class="name">
      <span>{{ allocation.user.rank }} {{ allocation.user.nickname }}</span>
      <UTooltip :text="`${allocation.user.rank} ${allocation.user.name} do ${allocation.user.company}`">
        <UBadge variant="soft">
          <UIcon :name="useIcon().information" />
        </UBadge>
      </UTooltip>
    </section>
    <section class="actions">
      <UTooltip text="Usuário é moderador?">
        <UToggle :on-icon="useIcon().check" :off-icon="useIcon().close" color="green" v-model="isModerator" />
      </UTooltip>
      <UTooltip text="Usuário é administrador?">
        <UToggle :on-icon="useIcon().check" :off-icon="useIcon().close" color="orange" v-model="isAdministrator" />
      </UTooltip>
      <UTooltip text="Excluir alocação">
        <UButton :icon="useIcon().trash" color="red" aria-label="Excluir" class="no-text" @click="askRemove" />
      </UTooltip>
    </section>
  </li>
</template>

<style lang="postcss" scoped>
li {
  @apply flex flex-row justify-between items-center space-x-2;
}

li > section.name {
  @apply flex flex-row justify-start items-center space-x-2;
}

li > section.name > span {
  @apply text-primary-600;
}

li > section.actions {
  @apply flex flex-row justify-end items-center space-x-2;
}
</style>
