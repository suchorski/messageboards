import { format, addMinutes, differenceInDays, differenceInHours, isBefore } from 'date-fns'
import { ptBR } from 'date-fns/locale'

export const useDate = () => {
  const toDateTimeString = (value: string | Date) => format(new Date(value), 'PPPp', { locale: ptBR })

  const toDateString = (value: string | Date) => {
    const date = new Date(value)
    return format(addMinutes(date, date.getTimezoneOffset()), `dd 'de' MMMM 'de' yyyy`, { locale: ptBR })
  }

  const toDaysOrHours = (value: string | Date): { value: number; type: 'days' | 'hours'; deadlined: boolean } => {
    const date = new Date(value)
    const days = differenceInDays(date, new Date())
    const hours = differenceInHours(date, new Date())
    const deadlined = isBefore(date, new Date())
    if (deadlined) return { value: Math.abs(days), type: 'days', deadlined }
    return days > 0 ? { value: days, type: 'days', deadlined } : { value: hours, type: 'hours', deadlined }
  }

  return { toDateTimeString, toDateString, toDaysOrHours }
}
