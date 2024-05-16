import { type ApiContent } from '../api'
import type { TUser } from './user'

export type TCommentDTO = {
  text: string
  message: { id: number }
}

export type TComment = {
  id?: number
  text: string
  creationDate: string
  deleted: boolean
  author: TUser
}

export type TComments = ApiContent<TComment>

export const useCommentApi = () => {
  const add = (comment: TCommentDTO) =>
    useApi<TComment>('/comments', { key: 'comments:add', method: 'post', body: comment }, false)

  const remove = (comment: TComment) =>
    useApi<boolean>(`/comments/${comment.id}`, { key: `comments:delete:${comment.id}`, method: 'delete' })

  const sorter = (left: TComment, right: TComment) =>
    new Date(right.creationDate).getTime() - new Date(left.creationDate).getTime()

  return { add, remove, sorter }
}
