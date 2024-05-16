<script lang="ts" setup>
const { logout } = useAuthStore()
const { boards } = storeToRefs(useBoardStore())

const allocations = computed(() => [
  boards.value.map((b) => ({
    label: b.name,
    to: `/quadro/${b.id}`,
  })),
])
</script>

<template>
  <nav>
    <ul>
      <li>
        <UDropdown v-if="allocations[0].length > 0" :items="allocations" :popper="{ placement: 'bottom-start' }">
          <p>Avisos</p>
          <template #item="{ item }">
            <span class="item-label">{{ item.label }}</span>
          </template>
        </UDropdown>
        <UDropdown v-else :items="[[{ label: 'Você não possui alocações.' }]]" :popper="{ placement: 'bottom-start' }">
          <p>Avisos</p>
        </UDropdown>
      </li>
      <li><ULink to="/quadros-de-avisos">Quadros de Avisos</ULink></li>
      <li><ULink to="/ajuda">Ajuda</ULink></li>
      <li><ULink to="/novidades">Novidades</ULink></li>
    </ul>
    <section>
      <ul>
        <li>
          <UTooltip text="Sair">
            <ULink @click="logout" class="logout-link">
              <UIcon :name="useIcon().logout" class="logout-icon" />
            </ULink>
          </UTooltip>
        </li>
      </ul>
    </section>
  </nav>
</template>

<style lang="postcss" scoped>
nav {
  @apply px-8 bg-primary-800 text-primary-200 flex flex-row justify-between;
}

nav ul {
  @apply flex-grow flex flex-col md:flex-row justify-start items-start md:items-center space-x-0 md:space-x-4 space-y-2 md:space-y-0;
}

nav ul li > * {
  @apply md:p-2 hover:bg-primary-700;
}

nav section {
  @apply flex flex-row justify-end items-center space-x-4;
}

.item-label {
  @apply truncate;
}

.item-icon {
  @apply flex-shrink-0 h-4 w-4 text-primary-800 dark:text-primary-200 ms-auto;
}

.logout-link {
  @apply flex flex-col items-center;
}

.logout-icon {
  @apply size-5;
}
</style>
