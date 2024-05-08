<template>
  <main>
    <section>
      <h1>Sistema de Quadros de Avisos</h1>
      <p>Seja bem vindo(a) ao Sistema de Quadros de avisos.</p>
    </section>
  </main>
</template>

<style lang="postcss" scoped>
main {
  @apply p-2 flex flex-col items-center;
}

section {
  @apply pt-16 flex flex-col items-center space-y-8;
}

h1 {
  @apply text-5xl font-bold;
}

p {
  @apply text-lg;
}
</style>
