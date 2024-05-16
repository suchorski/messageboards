import { format, addMinutes } from 'date-fns'
import { ptBR } from 'date-fns/locale'

export const useDate = () => {
  const toDateTime = (value: string | Date) => format(new Date(value), 'PPPp', { locale: ptBR })

  const toDate = (value: string | Date) => {
    const date = new Date(value)
    return format(addMinutes(date, date.getTimezoneOffset()), `dd 'de' MMMM 'de' yyyy`, { locale: ptBR })
  }

  return { toDateTime, toDate }
}
