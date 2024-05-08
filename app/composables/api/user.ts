import { type ApiContent } from '../api'

export type TUser = {
  id?: number
  name: string
  nickname: string
  rank: string
  om: string
}

export type TUsers = ApiContent<TUser>

export const useUserApi = () => {
  const sorter = (left: TUser, right: TUser) => left.name.localeCompare(right.name)

  return { sorter }
}
