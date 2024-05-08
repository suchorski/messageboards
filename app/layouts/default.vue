<template>
  <UContainer as="main">
    <Navigation />
    <section>
      <slot />
    </section>
    <Loading />
    <UNotifications />
  </UContainer>
</template>

<style lang="postcss" scoped>
.background {
  @apply min-h-screen bg-primary-50 dark:bg-gray-900;
}
</style>
