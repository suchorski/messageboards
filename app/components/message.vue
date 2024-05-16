<script lang="ts" setup>
import type { TMessage } from '~/composables/api/message'
import { isBefore, differenceInDays } from 'date-fns'

const props = defineProps<{ message: TMessage }>()
const emit = defineEmits<{
  (e: 'finalize', value: TMessage): void
  (e: 'remove', value: TMessage): void
}>()

const { danger } = useToaster()
const { show, hide } = useLoading()
const { toDateTime, toDate } = useDate()

const deadline = ref<Date>(new Date(props.message.deadline ?? Date.now()))
watch(deadline, async (newValue, oldValue) => {
  if (newValue !== oldValue) {
    const { updateDeadline } = useMessageApi()
    show()
    const { error } = await updateDeadline(props.message, newValue)
    hide()
    if (error.value) {
      danger('Erro ao atualizar o Prazo.', error.value?.data.message)
    } else {
      props.message.deadline = newValue.toISOString()
    }
  }
})

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

const askRemoveDeadline = () => {
  const { confirm } = useModals()
  confirm('Confirmação de Remoção', 'Deseja realmente remover o Prazo?', async () => {
    const { updateDeadline } = useMessageApi()
    show()
    const { error } = await updateDeadline(props.message)
    hide()
    if (error.value) {
      danger('Erro ao atualizar o Prazo.', error.value?.data.message)
    } else {
      props.message.deadline = null
    }
  })
}

const toComments = (id: number) => {
  const { push } = useRouter()
  push(`/mensagem/${id}`)
}

const deadlineColor = computed(() => {
  if (props.message.deadline === null) {
    return 'primary'
  }
  if (isBefore(new Date(props.message.deadline), Date.now())) {
    return 'rose'
  }
  if (differenceInDays(new Date(props.message.deadline), Date.now()) <= 7) {
    return 'amber'
  }
  return 'emerald'
})

const deadlineDays = computed(() => differenceInDays(new Date(props.message.deadline || Date.now()), Date.now()))
</script>

<template>
  <UCard :ui="{ body: { padding: 'p-0' }, header: { padding: 'px-4 py-2' }, footer: { padding: 'p-2' } }">
    <template #header>
      <header>
        <div class="title">
          <div class="subtitle">
            <h2>Aviso de {{ message.author.rank }} {{ message.author.nickname }}</h2>
            <UTooltip :text="`${message.author.rank} ${message.author.name} do ${message.author.company}`">
              <UBadge variant="soft">
                <UIcon :name="useIcon().information" />
              </UBadge>
            </UTooltip>
          </div>
          <p class="thin">Última atualização: {{ toDateTime(message.lastUpdateDate) }}</p>
          <p class="thin">Desde: {{ toDateTime(message.creationDate) }}</p>
          <div class="deadline">
            <Transition name="fade" mode="out-in">
              <p v-if="message.deadline !== null" key="deadlined" class="thin">
                Prazo: {{ toDate(message.deadline!) }}
              </p>
              <p v-else key="no-deadlined" class="thin">Prazo não definido.</p>
            </Transition>
            <UPopover :popper="{ placement: 'bottom-end' }">
              <UTooltip text="Editar prazo">
                <UButton variant="soft" color="orange" aria-label="Editar prazo" class="small-button">
                  <UIcon :name="useIcon().pencil" class="size-3" />
                </UButton>
              </UTooltip>
              <template #panel="{ close }">
                <DatePicker v-model="deadline" @close="close" />
              </template>
            </UPopover>
            <Transition name="fade" mode="out-in">
              <UTooltip text="Remover prazo" v-if="message.deadline !== null">
                <UButton
                  variant="soft"
                  color="red"
                  aria-label="Remover prazo"
                  class="small-button"
                  @click="askRemoveDeadline"
                >
                  <UIcon :name="useIcon().close" class="size-3" />
                </UButton>
              </UTooltip>
            </Transition>
          </div>
        </div>
        <Transition name="fade" mode="out-in">
          <UBadge v-if="message.deadline !== null" :color="deadlineColor">
            <Transition name="fade" mode="out-in">
              <div v-if="!message.finalizationDate" key="current" class="deadline-badge">
                <p v-if="deadlineDays >= 0">Prazo para</p>
                <p v-else>Vencido há</p>
                <p class="days">{{ Math.abs(deadlineDays) }}</p>
                <p>{{ Math.abs(deadlineDays) === 1 ? 'Dia' : 'Dias' }}</p>
              </div>
              <div v-else key="finalized" class="deadline-bagde">
                <p>Finalizado</p>
                <p>{{ toDateTime(message.finalizationDate) }}</p>
              </div>
            </Transition>
          </UBadge>
        </Transition>
      </header>
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
  @apply flex flex-row justify-between items-stretch space-x-2;
}

div.title {
  @apply flex flex-col justify-start;
}

div.subtitle {
  @apply flex flex-row justify-start items-center space-x-2;
}

div.deadline-info {
  @apply bg-red-600 flex flex-row justify-start items-center space-x-2;
}

p.thin {
  @apply font-thin text-sm text-primary-500;
}

div.deadline {
  @apply flex flex-row justify-start items-center space-x-2;
}

div.deadline-badge {
  @apply px-4 flex flex-col justify-between items-center space-y-1;
}

div.deadline-badge p.days {
  @apply font-bold text-lg;
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

.small-button {
  @apply px-4 h-4;
}
</style>
