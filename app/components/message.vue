<script lang="ts" setup>
import type { TMessage } from '~/composables/api/message'

const props = defineProps<{ message: TMessage }>()
const emit = defineEmits<{
  (e: 'finalize', value: TMessage): void
  (e: 'remove', value: TMessage): void
}>()

const { danger } = useToaster()
const { show, hide } = useLoading()
const { toDateTime } = useDate()

const askFinalize = () => {
  const { confirm } = useModals()
  confirm('Confirmação de Finalização', 'Deseja realmente finalizar o Aviso?', async () => {
    const { finalize } = useMessageApi()
    show()
    const { error } = await finalize(props.message)
    hide()
    if (error.value) {
      danger('Erro ao finalizar o Aviso.', error.value?.data.message)
    } else {
      emit('finalize', props.message)
    }
  })
}

const askRemove = () => {
  const { confirm } = useModals()
  confirm('Confirmação de Exclusão', 'Deseja realmente excluir o Aviso?', async () => {
    const { remove } = useMessageApi()
    show()
    const { error } = await remove(props.message)
    hide()
    if (error.value) {
      danger('Erro ao excluir o Aviso.', error.value?.data.message)
    } else {
      emit('remove', props.message)
    }
  })
}

const toComments = (id: number) => {
  const { push } = useRouter()
  push(`/mensagem/${id}`)
}
</script>

<template>
  <UCard :ui="{ body: { padding: 'p-0' }, header: { padding: 'px-4 py-2' }, footer: { padding: 'p-2' } }">
    <template #header>
      <header>
        <h2>Aviso de {{ message.author.rank }} {{ message.author.nickname }}</h2>
        <UTooltip :text="`${message.author.rank} ${message.author.name} do ${message.author.om}`">
          <UBadge variant="soft">
            <UIcon :name="useIcon().information" />
          </UBadge>
        </UTooltip>
      </header>
      <p>Última atualização: {{ toDateTime(message.lastupdateDate) }}</p>
      <p>Desde: {{ toDateTime(message.creationDate) }}</p>
    </template>
    <Viewer v-model="message.text" />
    <template #footer>
      <section class="actions">
        <UTooltip text="Listar Comentários">
          <UButton
            :icon="useIcon().chat"
            color="primary"
            block
            aria-label="Comentários"
            class="no-text"
            @click="toComments(message.id!)"
          />
          <UButton :icon="useIcon().chat" color="primary" block class="text" @click="toComments(message.id!)"
            >Comentários</UButton
          >
        </UTooltip>
        <UTooltip text="Finalizar Aviso">
          <UButton
            :icon="useIcon().ok"
            color="green"
            block
            aria-label="Finalizar"
            class="no-text"
            @click="askFinalize"
          />
          <UButton :icon="useIcon().ok" color="green" block class="text" @click="askFinalize">Finalizar</UButton>
        </UTooltip>
        <UTooltip text="Excluir Aviso">
          <UButton :icon="useIcon().trash" color="red" block aria-label="Excluir" class="no-text" @click="askRemove" />
          <UButton :icon="useIcon().trash" color="red" block class="text" @click="askRemove">Excluir</UButton>
        </UTooltip>
      </section>
    </template>
  </UCard>
</template>

<style lang="postcss" scoped>
header {
  @apply flex flex-row justify-start items-center space-x-2;
}

p {
  @apply font-thin text-sm text-primary-500;
}

section.actions {
  @apply flex flex-row justify-between space-x-2;
}

section.actions > * {
  @apply flex-1;
}

.no-text {
  @apply xl:hidden;
}

.text {
  @apply hidden xl:flex;
}
</style>
