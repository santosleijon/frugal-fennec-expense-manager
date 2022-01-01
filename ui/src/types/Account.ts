import { Expense } from "./Expense";

export interface Account {
  id?: string
  name: string
  expenses: Expense[]
}