import { format, addMinutes } from 'date-fns'
import { ptBR } from 'date-fns/locale'

export const useDate = () => {
  const toDateTime = (value?: Date) => format(value || new Date(), 'PPPp', { locale: ptBR })

  const toDate = (value: Date) => {
    const date = new Date(value)
    return format(addMinutes(date, date.getTimezoneOffset()), `dd 'de' MMMM 'de' yyyy`, { locale: ptBR })
  }

  return { toDateTime, toDate }
}
