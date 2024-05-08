import { format } from 'date-fns'
import { ptBR } from 'date-fns/locale'

export const useDate = () => {
  const toDateTime = (value?: Date) => format(value || new Date(), 'PPPp', { locale: ptBR })

  return { toDateTime }
}
