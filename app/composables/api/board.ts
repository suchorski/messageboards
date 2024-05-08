import { type ApiContent } from '../api'

export type TBoard = {
  id?: number
  name: string
}

export type TBoards = ApiContent<TBoard>

export const useBoardApi = () => {
  const add = (board: TBoard) => useApi<TBoard>('/boards', { key: 'boards:add', method: 'post', body: board }, false)

  const list = () => useApi<TBoards>('/boards/list', { key: 'boards:list:mine', method: 'get' })

  const updateName = (board: TBoard, newName: string) => {
    const updatedBoard = { ...board, name: newName }
    return useApi<TBoard>(`/boards/${board.id}`, {
      key: `boards:update:${board.id}`,
      method: 'put',
      body: updatedBoard,
    })
  }

  const remove = (board: TBoard) =>
    useApi<void>(`/boards/${board.id}`, { key: `boards:delete:${board.id}`, method: 'delete' })

  const sorter = (left: TBoard, right: TBoard) => left.name.localeCompare(right.name)

  return { add, list, updateName, remove, sorter }
}
