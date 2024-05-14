import { defineStore } from 'pinia'

export interface TMessageStore {
  text: Ref<string>
  deadlined: Ref<boolean>
  deadline: Ref<Date>
}

export const messageStoreName = 'messageStore'

export const useMessageStore = defineStore<string, TMessageStore>(
  messageStoreName,
  () => {
    const text = ref<string>('')
    const deadlined = ref<boolean>(false)
    const deadline = ref<Date>(new Date())

    return { text, deadlined, deadline }
  },
  { persist: { storage: localStorage } }
)
