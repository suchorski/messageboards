<script lang="ts" setup></script>

<template>
  <main>
    <section>
      <h1>Sistema de Quadros de Avisos</h1>
      <p>Seja bem vindo(a) ao Sistema de Quadros de avisos.</p>
    </section>
    <section>
      <h1>Gerenciamento dos Avisos</h1>
      <h2>Inclusão de avisos</h2>
      <p>Para incluir um novo aviso, acesse o quadro de avisos desejado e clique em 'Adicionar Novo Aviso'.</p>
      <h2>Edição de avisos</h2>
      <p>Não é possível editar um aviso.</p>
      <h2>Exclusão de avisos</h2>
      <p>Para excluir um aviso, acesse o quadro de avisos desejado e clique em 'Excluir'.</p>
      <p class="warn">
        Somente o autor do aviso ou um administrador do quadro de avisos pode excluí-lo e se não tiver comentários ou
        finalizado.
      </p>
      <h2>Finalizar um aviso</h2>
      <p>Para finalizar um aviso, acesse o quadro de avisos desejado, vizualize o aviso e clique em 'Finalizar'.</p>
      <p class="warn">Somente o autor do aviso ou um administrador do quadro de avisos pode finalizar o aviso.</p>
      <h2>Vizualizar comentários</h2>
      <p>
        Para vizualizar os comentários de um aviso, acesse o quadro de avisos desejado, vizualize o aviso e clique em
        'Comentários'.
      </p>
    </section>
    <section>
      <h1>Gerenciamento dos Quadros de Avisos</h1>
      <h2>Inclusão de quadros de avisos</h2>
      <p>
        Para incluir um novo quadro de avisos, clique em 'Quadros de Avisos' e em seguida em 'Adicionar Novo Quadro'.
      </p>
      <h2>Edição de quadros de avisos</h2>
      <p>Não é possível editar um aviso.</p>
      <h2>Exclusão de quadros de avisos</h2>
      <p>
        Para excluir um quadro de avisos, clique em 'Quadros de Avisos', vizualize o quadro de aviso e em seguida em
        'Excluir'.
      </p>
      <p class="warn">
        Somente os administradores podem excluí-los e não é possível excluir um quadro de avisos que contenha avisos.
      </p>
      <h2>Alocar usuários ao quadro de aviso</h2>
      <p>
        Para alocar usuários ao quadro de avisos, clique em 'Quadros de Avisos', vizualize o quadro de aviso e em
        seguida em 'Alocar Usuário'.
      </p>
      <p class="warn">Somente um moderador ou administrador pode alocar usuários ao quadro de avisos.</p>
      <h2>Alterar alocação no quadro de avisos</h2>
      <p>
        Para alterar a alocação de um usuário no quadro de avisos, clique em 'Quadros de Avisos', vizualize o quadro de
        aviso e em seguida em 'Alterar Alocação'.
      </p>
      <p class="warn">Somente um administrador pode alterar alocações de usuários no quadro de avisos.</p>
      <h2>Excluir alocação no quadro de avisos</h2>
      <p>
        Para excluir a alocação de um usuário no quadro de avisos, clique em 'Quadros de Avisos', vizualize o quadro de
        aviso, em seguida em 'Alterar Alocação' e finalmente exclua o usuário desejado.
      </p>
      <p class="warn">Somente um administrador pode excluir alocações de usuários no quadro de avisos.</p>
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

.warn {
  @apply text-red-700;
}
</style>
