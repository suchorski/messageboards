<template>
  <div class="skeleton">
    <div class="text">
      <USkeleton class="line-wider" />
      <USkeleton class="line-narrow" />
    </div>
    <div class="actions">
      <USkeleton class="action" />
      <USkeleton class="action" />
      <USkeleton class="action" />
      <USkeleton class="action" />
    </div>
  </div>
</template>

<style lang="postcss" scoped>
.skeleton {
  @apply flex justify-between items-center space-x-2;
}

.text {
  @apply space-y-2;
}

.line-wider {
  @apply h-4 w-64;
}

.line-narrow {
  @apply h-4 w-40;
}

.actions {
  @apply flex flex-row space-x-2;
}

.action {
  @apply size-12;
}
</style>
