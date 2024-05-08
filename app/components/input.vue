<script lang="ts" setup>
defineProps<{ title: string; message: string }>()
defineEmits<{
  (e: 'cancel'): void
  (e: 'confirm', value: string): string
}>()

const input = ref<string>('')
</script>

<template>
  <UModal>
    <UCard>
      <template #header>
        <header>
          <h1>{{ title }}</h1>
          <UButton :icon="useIcon().close" variant="soft" aria-label="Fechar" @click="$emit('cancel')" />
        </header>
      </template>
      <section>
        <p>{{ message }}</p>
        <UInput v-model="input" autofocus @keyup.enter="$emit('confirm', input)" />
      </section>
      <template #footer>
        <footer>
          <UButton color="gray" label="Cancelar" block @click="$emit('cancel')" />
          <UButton label="Confirmar" block @click="$emit('confirm', input)" :disabled="input.trim().length === 0" />
        </footer>
      </template>
    </UCard>
  </UModal>
</template>

<style lang="postcss" scoped>
header {
  @apply flex flex-row justify-between items-center;
}

section {
  @apply flex flex-col space-y-2;
}

footer {
  @apply flex flex-row justify-between space-x-2;
}

footer > * {
  @apply flex-1;
}
</style>
