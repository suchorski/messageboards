<script lang="ts" setup>
import Quill from 'quill/core'
import { QuillyEditor } from 'vue-quilly'

const options = { readOnly: true }

const editor = ref<InstanceType<typeof QuillyEditor>>()
onMounted(() => editor.value?.initialize(Quill)!)

const model = defineModel<string>()
</script>

<template>
  <QuillyEditor ref="editor" v-model="model" :options="options" />
</template>

<style lang="postcss" scoped>
main {
  @apply py-2 space-y-2;
}
</style>
