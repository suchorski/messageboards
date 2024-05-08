<template>
  <div class="skeleton">
    <USkeleton class="line" />
    <div class="actions">
      <USkeleton class="type" />
      <USkeleton class="type" />
      <USkeleton class="exclude" />
    </div>
  </div>
</template>

<style lang="postcss" scoped>
.skeleton {
  @apply flex justify-between items-center space-x-2;
}

.line {
  @apply h-4 w-40;
}

.actions {
  @apply flex flex-row justify-end items-center space-x-2;
}

.actions .type {
  @apply w-8 h-6;
}

.actions .exclude {
  @apply size-6;
}
</style>
