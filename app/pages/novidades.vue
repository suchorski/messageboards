<script lang="ts" setup>
const updates = [
  {
    version: '1.1.0',
    news: [
      'Adição de opção para alterar o nome do Quadro de Avisos.',
      'Adição de opção alternativa de LDAP para atualização de dados de usuários.',
      'Adição de um temporizador para atualização de dados de usuários.',
      'Dados do usuário são atualizados a cada requisição.',
      'Atualização de dependências.',
      'Correção de bugs.',
    ],
  },
  { version: '1.0.0', news: ['Versão inicial de lançamento do sistema.'] },
]
const items = computed(() => updates.map((u) => ({ label: `Versão: ${u.version}`, changelog: u.news })))
</script>

<template>
  <main>
    <section>
      <h1>Sistema de Quadros de Avisos</h1>
      <p>Criado por 2S Suchorski - PAMA LS</p>
      <h2>Versão: {{ updates[0].version }}</h2>
    </section>
    <UDivider />
    <section>
      <h1>Notas da Versão</h1>
      <UAccordion :items>
        <template #item="{ item }">
          <ul>
            <li v-for="i in item.changelog" :key="i">{{ i }}</li>
          </ul>
        </template>
      </UAccordion>
    </section>
  </main>
</template>

<style lang="postcss" scoped>
main {
  @apply p-2 space-y-4;
}

section {
  @apply space-y-2;
}

p {
  @apply font-thin text-sm text-primary-500;
}

ul {
  @apply list-disc list-inside;
}

li {
  @apply pl-2;
}
</style>
