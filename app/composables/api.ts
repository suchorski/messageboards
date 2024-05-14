export { useBoardApi } from './api/board'
export { useAllocationApi } from './api/allocation'
export { useMessageApi } from './api/message'
export { useCommentApi } from './api/comment'

interface SearchParameter {
  [key: string]: any
}

export interface FetchOptions {
  key: string
  method: 'get' | 'post' | 'put' | 'delete'
  query?: SearchParameter | Ref<SearchParameter | undefined>
  body?: Record<string, any>
}

export type ApiContent<TData> = {
  content: TData[]
  size?: number
  totalElements?: number
  totalPages?: number
  number?: number
}

export interface Pagination {
  page: number
  size: number
  sort: string
}

export const useApi = <T>(url: string, opts: FetchOptions, watch?: false) => {
  const { fine } = useLogger()
  const { public: config } = useRuntimeConfig()

  fine('useFetchApi called:', url, opts)

  return useFetch<T>(url, {
    query: opts.query,
    body: opts.body,
    key: opts.key,
    baseURL: config.API_ENTRYPOINT,
    watch,

    onRequest({ options }) {
      fine('onRequest called:', options)
      options.method = opts?.method
      options.headers = new Headers([['Authorization', `Bearer ${useTokenStore().token || ''}`]])
    },

    onResponse({ response }) {
      fine('onResponse called:', response)
      if (response.status === 401) {
        fine('onResponse called: 401', response)
        useAuthStore().logout()
        useRouter().push('/')
      } else if (response.status === 403) {
        fine('onResponse called: 403', response)
        useRouter().push('/forbidden')
      }
    },
  })
}
