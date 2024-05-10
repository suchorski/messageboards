import { type ApiContent } from '../api'
import type { TComment } from './comment'
import type { TUser } from './user'

export type TMessageDTO = {
  text: string
  deadline: Date | null
  board: { id: number }
}

export type TMessage = {
  id?: number
  text: string
  creationDate: Date
  lastUpdateDate: Date
  finalizationDate: Date
  deadline: Date | null
  author: TUser
}

export type TMessageComments = {
  id?: number
  text: string
  creationDate: Date
  lastupdateDate: Date
  finalizationDate: Date
  deadline: Date | null
  author: TUser
  comments: TComment[]
}

export type TMessages = ApiContent<TMessage>

export const useMessageApi = () => {
  const add = (message: TMessageDTO) =>
    useApi<TMessage>('/messages', { key: 'messages:add', method: 'post', body: message }, false)

  const get = (id: number) => useApi<TMessageComments>(`/messages/${id}`, { key: `messages:get:${id}`, method: 'get' })

  const listByBoardId = (id: number) =>
    useApi<TMessage[]>(`/messages/byBoardId/${id}`, { key: `messages:list:${id}`, method: 'get' })

  const updateDeadline = (message: TMessage, deadline?: Date) =>
    useApi<void>(`/messages/${message.id}/deadline`, {
      key: `messages:updateDeadline:${message.id}`,
      method: 'put',
      body: { deadline },
    })

  const finalize = (message: TMessage) =>
    useApi<TMessage>(`/messages/${message.id}`, { key: `messages:finalize:${message.id}`, method: 'put' })

  const remove = (message: TMessage) =>
    useApi<void>(`/messages/${message.id}`, { key: `messages:delete:${message.id}`, method: 'delete' })

  const sorter = (left: TMessage, right: TMessage) =>
    new Date(left.lastUpdateDate).getTime() - new Date(right.lastUpdateDate).getTime()

  const sorterDesc = (left: TMessage, right: TMessage) =>
    new Date(right.lastUpdateDate).getTime() - new Date(left.lastUpdateDate).getTime()

  const creationDateSorter = (left: TMessage, right: TMessage) =>
    new Date(left.creationDate).getTime() - new Date(right.creationDate).getTime()

  const creationDateSorterDesc = (left: TMessage, right: TMessage) =>
    new Date(right.creationDate).getTime() - new Date(left.creationDate).getTime()

  const deadlineDateSorter = (left: TMessage, right: TMessage) => {
    if (left.deadline === right.deadline) return 0
    if (left.deadline === null) return 1
    if (right.deadline === null) return -1
    return new Date(left.deadline).getTime() - new Date(right.deadline).getTime()
  }
  const deadlineDateSorterDesc = (left: TMessage, right: TMessage) => {
    if (left.deadline === right.deadline) return 0
    if (left.deadline === null) return -1
    if (right.deadline === null) return 1
    return new Date(right.deadline).getTime() - new Date(left.deadline).getTime()
  }

  return {
    add,
    get,
    listByBoardId,
    updateDeadline,
    finalize,
    remove,
    sorter,
    sorterDesc,
    creationDateSorter,
    creationDateSorterDesc,
    deadlineDateSorter,
    deadlineDateSorterDesc,
  }
}
