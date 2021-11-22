export interface Account {
  number: number
  name?: string
}

export interface Expense {
  id: number
  date: string
  account: Account
  description: string
  amount: number
}