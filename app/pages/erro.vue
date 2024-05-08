<script lang="ts" setup>
const message = useRoute().query.mensagem as string
</script>

<template>
  <main>
    <h1>Erro Desconhecido</h1>
    <Alert title="Algo estranho aconteceu!" :description="message" error />
  </main>
</template>

<style lang="postcss" scoped>
main {
  @apply p-8 flex flex-col items-center space-y-12;
}

h1 {
  @apply text-4xl;
}
</style>
