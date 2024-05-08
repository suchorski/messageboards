<script lang="ts" setup>
defineProps<{ title: string; message: string }>()
defineEmits<{
  (e: 'cancel'): void
  (e: 'confirm'): void
}>()
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
      </section>
      <template #footer>
        <footer>
          <UButton color="gray" label="Cancelar" block @click="$emit('cancel')" />
          <UButton label="Confirmar" block @click="$emit('confirm')" />
        </footer>
      </template>
    </UCard>
  </UModal>
</template>

<style lang="postcss" scoped>
header {
  @apply flex flex-row justify-between items-center;
}

footer {
  @apply flex flex-row justify-between space-x-2;
}

footer > * {
  @apply flex-1;
}
</style>
