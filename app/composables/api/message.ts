import { type ApiContent } from '../api'
import type { TComment } from './comment'
import type { TUser } from './user'

export type TMessageDTO = {
  text: string
  board: { id: number }
}

export type TMessage = {
  id?: number
  text: string
  creationDate: Date
  lastupdateDate: Date
  finalizationDate: Date
  author: TUser
}

export type TMessageComments = {
  id?: number
  text: string
  creationDate: Date
  lastupdateDate: Date
  finalizationDate: Date
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

  const finalize = (message: TMessage) =>
    useApi<TMessage>(`/messages/${message.id}`, { key: `messages:finalize:${message.id}`, method: 'put' })

  const remove = (message: TMessage) =>
    useApi<void>(`/messages/${message.id}`, { key: `messages:delete:${message.id}`, method: 'delete' })

  const sorter = (left: TMessage, right: TMessage) =>
    new Date(left.lastupdateDate).getTime() - new Date(right.lastupdateDate).getTime()

  return { add, get, listByBoardId, finalize, remove, sorter }
}
