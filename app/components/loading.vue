<script lang="ts" setup>
const showingLoading = useState(useLoadingKey(), () => false)
</script>

<template>
  <UModal v-model="showingLoading" prevent-close appear>
    <div>
      <UIcon :name="useIcon().loading" />
    </div>
  </UModal>
</template>

<style lang="postcss" scoped>
div {
  @apply w-full h-64 flex items-center justify-center;
}

div span {
  @apply text-primary w-2/3 h-2/3 animate-spin;
}
</style>
