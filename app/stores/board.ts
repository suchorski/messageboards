import { defineStore } from 'pinia'
import type { TBoard } from '~/composables/api/board'

export interface TBoardStore {
  loaded: Ref<boolean>
  boards: Ref<TBoard[]>
  removeBoard: (board: TBoard) => void
}

export const boardStoreName = 'boardStore'

export const useBoardStore = defineStore<string, TBoardStore>(boardStoreName, () => {
  const loaded = ref<boolean>(false)
  const boards = ref<TBoard[]>([])

  const removeBoard = (board: TBoard) => {
    const index = boards.value.indexOf(board)
    if (index !== undefined) {
      boards.value.splice(index, 1)
    }
  }

  return { loaded, boards, removeBoard }
})
