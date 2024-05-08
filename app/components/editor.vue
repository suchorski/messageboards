<script lang="ts" setup>
import Quill from 'quill'
import { QuillyEditor } from 'vue-quilly'

const options = {
  theme: 'snow',
  modules: {
    toolbar: [
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      [{ font: [] }],
      ['bold', 'italic', 'underline', 'strike'],
      [{ align: [] }],
      [{ color: [] }, { background: [] }],
      [{ script: 'sub' }, { script: 'super' }],
      [{ indent: '-1' }, { indent: '+1' }],
      ['clean'],
    ],
  },
}

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
