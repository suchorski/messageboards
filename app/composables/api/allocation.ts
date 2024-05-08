import { type ApiContent } from '../api'
import { type TUser } from './user'
import { type TBoard } from './board'

export type TAllocation = {
  id?: number
  user: TUser
  board?: TBoard
  administrator: boolean
  moderator: boolean
}

export type TAllocationDTO = {
  cpf: string
  board: { id: number }
  administrator: boolean
  moderator: boolean
}

export type TAllocations = ApiContent<TAllocation>

export const useAllocationApi = () => {
  const add = (allocation: TAllocationDTO) =>
    useApi<TAllocation>('/allocations', { key: 'allocations:add', method: 'post', body: allocation }, false)

  const listByBoardId = (id: number) =>
    useApi<TAllocation[]>(`/allocations/byBoardId/${id}`, { key: `allocations:list:${id}`, method: 'get' })

  const update = (id: number, moderator: boolean, administrator: boolean) =>
    useApi<TAllocation>(
      `/allocations/${id}`,
      { key: `allocations:update:${id}`, method: 'put', body: { moderator, administrator } },
      false
    )

  const remove = (allocation: TAllocation) =>
    useApi<void>(`/allocations/${allocation.id}`, {
      key: `allocations:remove:${allocation.id}`,
      method: 'delete',
    })

  const sorter = (left: TAllocation, right: TAllocation) => left.user.nickname.localeCompare(right.user.nickname)

  return { add, listByBoardId, update, remove, sorter }
}
