export interface Account {
  number: number
  name?: string
}

export interface Transaction {
  id: number
  date: string
  account: Account
  description: string
  amount: number
}