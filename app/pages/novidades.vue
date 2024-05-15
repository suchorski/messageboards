<script lang="ts" setup>
const updates = [
  {
    version: '1.3.0',
    news: [
      'A mensagem é salva em rascunho até ser enviada.',
      'Ordenção padrão das mensagens alterada para o prazo mais curto.',
      'Atualização de dependências.',
    ],
  },
  {
    version: '1.2.3',
    news: ['Adicionado indicador de progressão de carregamento.'],
  },
  {
    version: '1.2.2',
    news: ['Adicionado efeito na barra de ordenação de avisos.'],
  },
  {
    version: '1.2.1',
    news: ['Corrigido texto para o singular quando o prazo era 1 dia.', 'Atualização de dependências.'],
  },
  {
    version: '1.2.0',
    news: [
      'Adição de prazo para a mensagens.',
      'Adição de opção para ordenar mensagens por data de criação, última atualização e prazo.',
      'Mudança na forma de atualização de novos usuários.',
      'Melhoria na performance do sistema.',
      'Fixado mensagem de erro remover mensagem com comentários.',
      'Usuários são atualizados somente após ter 1 dia de desatualização.',
      'Atualização de dependências.',
      'Correção de bugs.',
    ],
  },
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
