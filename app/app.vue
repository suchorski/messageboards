<script lang="ts" setup>
useHead({ title: 'Sistema de Quadros de Avisos' })
</script>

<template>
  <NuxtLayout>
    <NuxtPage />
    <UModals />
  </NuxtLayout>
</template>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: all 0.2s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  filter: blur(0.5rem);
}

.rotate-enter-active,
.rotate-leave-active {
  transition: all 0.2s;
}
.rotate-enter-from,
.rotate-leave-to {
  opacity: 0;
  transform: rotate(180deg);
}
</style>
