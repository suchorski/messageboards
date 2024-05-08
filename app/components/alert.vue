<script lang="ts" setup>
const props = withDefaults(
  defineProps<{
    title: string
    primary?: boolean
    success?: boolean
    warning?: boolean
    error?: boolean
    description?: string
  }>(),
  {
    primary: true,
    success: false,
    warning: false,
    error: false,
  }
)

const color = computed(() => {
  if (props.error) return 'red'
  if (props.warning) return 'yellow'
  if (props.success) return 'green'
  return 'primary'
})

const icon = computed(() => {
  const { error, warning, success, information } = useIcon()
  if (props.error) return error
  if (props.warning) return warning
  if (props.success) return success
  return information
})
</script>

<template>
  <UAlert :title :color :icon :description>
    <template #icon="{ icon }">
      <UBadge :color size="lg">
        <UIcon :name="icon" class="size-6" />
      </UBadge>
    </template>
  </UAlert>
</template>
