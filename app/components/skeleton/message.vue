<template>
  <div class="skeleton">
    <div class="header">
      <USkeleton class="line-wider" />
      <USkeleton class="line-narrow" />
      <USkeleton class="line-narrow" />
    </div>
    <USkeleton class="text" />
    <div class="actions">
      <USkeleton class="action" />
      <USkeleton class="action" />
      <USkeleton class="action" />
    </div>
  </div>
</template>

<style lang="postcss" scoped>
.skeleton {
  @apply flex flex-col space-y-2;
}

.header {
  @apply space-y-2;
}

.line-wider {
  @apply h-4 w-80;
}

.line-narrow {
  @apply h-4 w-60;
}

.text {
  @apply h-16 w-full;
}

.actions {
  @apply flex flex-row space-x-2;
}

.action {
  @apply flex-1 h-8;
}
</style>
