import { Account } from "./Account";

export interface Expense {
  id: number
  date: string
  account: Account
  description: string
  amount: number
}