<script lang="ts" setup>
import type { TComment } from '~/composables/api/comment'

const { danger } = useToaster()
const { show, hide } = useLoading()
const { toDateTime } = useDate()

const props = withDefaults(defineProps<{ comment: TComment; last: boolean }>(), { last: false })
const emit = defineEmits<{
  (e: 'remove', value: { comment: TComment; removed: boolean }): void
}>()

const askRemove = () => {
  const { confirm } = useModals()
  confirm('Confirmação de Exclusão', 'Deseja realmente excluir o comentário?', async () => {
    const { remove } = useCommentApi()
    show()
    const { data, error } = await remove(props.comment)
    hide()
    if (error.value) {
      danger('Erro ao excluir o comentário.', error.value?.data.message)
    } else {
      emit('remove', { comment: props.comment, removed: data.value! })
    }
  })
}
</script>

<template>
  <UCard :ui="{ body: { padding: 'px-4 py-2' } }">
    <header>
      <div class="title">
        <div class="name">
          <h3>Comentário de {{ comment.author.rank }} {{ comment.author.nickname }}</h3>
          <UTooltip :text="`${comment.author.rank} ${comment.author.name} do ${comment.author.company}`">
            <UBadge variant="soft">
              <UIcon :name="useIcon().information" />
            </UBadge>
          </UTooltip>
        </div>
        <span class="datetime">{{ toDateTime(comment.creationDate) }}</span>
      </div>
      <Transition name="rotate" mode="out-in">
        <UTooltip text="Excluir Comentário" v-if="last">
          <UButton :icon="useIcon().trash" color="red" block aria-label="Excluir" class="mt-2" @click="askRemove" />
        </UTooltip>
        <UTooltip text="Invalidar Comentário" v-else-if="!comment.deleted">
          <UButton :icon="useIcon().close" color="red" block aria-label="Invalidar" class="mt-2" @click="askRemove" />
        </UTooltip>
      </Transition>
    </header>
    <p v-if="comment.deleted" class="line-through">{{ comment.text }}</p>
    <p v-else>{{ comment.text }}</p>
  </UCard>
</template>

<style lang="postcss" scoped>
header {
  @apply flex flex-row justify-between items-start space-x-2;
}

div.title {
  @apply flex flex-col space-y-0;
}

div.name {
  @apply flex flex-row justify-start items-center space-x-2;
}

span.datetime {
  @apply font-thin text-sm text-primary-500;
}

p {
  @apply pt-4;
}
</style>
