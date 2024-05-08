import Confirm from '~/components/confirm.vue'
import Input from '~/components/input.vue'

export const useModals = () => {
  const modal = useModal()

  const confirm = (title: string, message: string, callback: () => void) => {
    modal.open(Confirm, {
      title: title,
      message: message,
      onConfirm: () => {
        modal.close()
        callback()
      },
      onCancel: () => modal.close(),
    })
  }

  const prompt = (title: string, message: string, callback: (value: string) => void) => {
    modal.open(Input, {
      title: title,
      message: message,
      onConfirm: (value: string) => {
        modal.close()
        callback(value)
      },
      onCancel: () => modal.close(),
    })
  }

  return { confirm, prompt }
}
