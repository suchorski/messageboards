<script lang="ts" setup>
import type { TComment } from '~/composables/api/comment'

const { get } = useMessageApi()
const { sorter } = useCommentApi()

const { success, warning } = useToaster()
const { toDateTimeString } = useDate()

const boardId: number = Number(useRoute().params.id)
const { data: stateData, pending: statePending, error: stateError } = get(boardId)
watch(stateData, (newValue, oldValue) => {
  if (newValue && newValue !== oldValue && stateError.value === null) {
    stateData.value?.comments.sort(sorter)
  }
})

const comment = ref<string>('')
const add = async () => {
  const { add } = useCommentApi()
  const { data, error } = await add({ text: comment.value, message: { id: boardId } })
  if (error.value) {
    warning('Erro ao adicionar o Comentário.', error.value?.data.message)
  } else {
    comment.value = ''
    success('Comentário adicionado com sucesso.')
    stateData.value!.comments.unshift(data.value!)
  }
}

const remove = (commentInfo: { comment: TComment; removed: boolean }) => {
  const index = stateData.value?.comments.indexOf(commentInfo.comment)
  if (index !== undefined) {
    if (commentInfo.removed) {
      stateData.value?.comments.splice(index, 1)
      success('Comentário excluído com sucesso.')
    } else {
      stateData.value!.comments[index].deleted = true
      success('Comentário invalidado com sucesso.')
    }
  } else {
    warning('Comentário não encontrado.')
  }
}
</script>

<template>
  <main>
    <Transition name="fade" mode="out-in">
      <section v-if="statePending" key="loading" class="loading">
        <SkeletonMessage v-for="i in 4" :key="i" />
      </section>
      <Alert
        v-else-if="stateError !== null"
        key="error"
        title="Erro ao carregar o aviso"
        :description="stateError.data.message || 'Erro desconhecido.'"
        error
      />
      <section class="data" v-else key="data">
        <UCard :ui="{ body: { padding: 'p-0' }, header: { padding: 'px-4 py-2' }, footer: { padding: 'p-2' } }">
          <template #header>
            <header class="message">
              <h2>Aviso de {{ stateData!.author.rank }} {{ stateData!.author.nickname }}</h2>
              <UTooltip :text="`${stateData!.author.rank} ${stateData!.author.name} do ${stateData!.author.company}`">
                <UBadge variant="soft">
                  <UIcon :name="useIcon().information" />
                </UBadge>
              </UTooltip>
            </header>
            <p class="header">Última atualização: {{ toDateTimeString(stateData!.lastUpdateDate) }}</p>
            <p class="header">Desde: {{ toDateTimeString(stateData!.creationDate) }}</p>
            <p class="header" v-if="stateData!.deadline">Prazo: {{ toDateTimeString(stateData!.deadline) }}</p>
          </template>
          <Viewer v-model="stateData!.text" />
          <template #footer>
            <footer class="message">
              <h2>Adicionar Comentário:</h2>
              <UInput v-model="comment" @keypress.enter="add" />
              <UButton @click="add" block :disabled="comment.trim().length === 0">Salvar Comentário</UButton>
            </footer>
          </template>
        </UCard>
        <Transition name="fade" mode="out-in">
          <section v-if="stateData!.comments.length > 0" key="comments" class="comments" v-auto-animate>
            <h1>Comentários</h1>
            <Comment
              v-for="(c, idx) in stateData!.comments"
              :key="c.id"
              :comment="c"
              :last="idx === 0"
              @remove="remove"
            />
          </section>
          <Alert
            v-else
            key="no-comments"
            title="Nenhum Comentário"
            description="Essa mensagem ainda não possui comentários."
          />
        </Transition>
      </section>
    </Transition>
  </main>
</template>

<style lang="postcss" scoped>
main {
  @apply p-2 flex flex-col space-y-2;
}

header.message {
  @apply flex flex-row justify-start items-center space-x-2;
}

p.header {
  @apply font-thin text-sm text-primary-500;
}

footer.message {
  @apply flex flex-col space-y-2;
}

section.data {
  @apply flex flex-col space-y-4;
}

section.comments {
  @apply flex flex-col space-y-2;
}
</style>
